package com.ruoyi.web.service;

import com.ideabank.sap.webservice.TokenCheckServiceService;
import com.ideabank.sap.webservice.TokenCheckWS;
import com.ideabank.sap.webservice.VerifyIdTRet;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.SysPermissionService;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.ws.BindingProvider;
import java.io.StringReader;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/** SAP2000 挑战/验票与本地账号令牌的桥接。 */
@Service
public class CaLoginService {
    private static final Logger log = LoggerFactory.getLogger(CaLoginService.class);
    private static final String CHALLENGE_PREFIX = "ca:challenge:";
    private static final int CHALLENGE_TTL_SECONDS = 120;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${ca.wsdl-url:}") private String wsdlUrl;
    @Value("${ca.app-server-id:}") private String appServerId;
    @Value("${ca.server-ip:}") private String serverIp;
    @Value("${ca.server-port:9021}") private int serverPort;
    @Value("${ca.mock-enabled:false}") private boolean mockEnabled;
    @Value("${ca.success-results:0,success,true}") private String successResults;

    @Autowired private RedisCache redisCache;
    @Autowired private ISysUserService userService;
    @Autowired private SysPermissionService permissionService;
    @Autowired private TokenService tokenService;
    @Autowired private SysLoginService loginService;
    @Autowired private CaBindingService caBindingService;

    public static class CaIdentity {
        private final String rmsId;
        private final String certSn;

        public CaIdentity(String rmsId, String certSn) {
            this.rmsId = rmsId;
            this.certSn = certSn;
        }

        public String getRmsId() { return rmsId; }
        public String getCertSn() { return certSn; }
    }

    public Map<String, Object> challenge() {
        checkConfiguration();
        String challenge;
        if (mockEnabled) {
            byte[] bytes = new byte[32];
            secureRandom.nextBytes(bytes);
            challenge = Base64.getEncoder().encodeToString(bytes);
        } else {
            challenge = client().generatorChallenge();
        }
        if (StringUtils.isBlank(challenge)) {
            throw new ServiceException("CA挑战值获取失败");
        }
        String id = UUID.randomUUID().toString();
        redisCache.setCacheObject(CHALLENGE_PREFIX + id, challenge, CHALLENGE_TTL_SECONDS, TimeUnit.SECONDS);
        Map<String, Object> response = new HashMap<>();
        response.put("challengeId", id);
        response.put("challenge", challenge);
        response.put("mock", mockEnabled);
        response.put("serverIp", mockEnabled ? "" : serverIp);
        response.put("serverPort", serverPort);
        return response;
    }

    public String login(String challengeId, String identityTicket) {
        CaIdentity identity = verifyIdentity(challengeId, identityTicket);
        Long userId = caBindingService.findUserId(identity);
        if (userId == null) {
            throw new ServiceException("该 UKey 尚未绑定系统用户");
        }
        SysUser user = userService.selectUserById(userId);
        if (user == null || UserStatus.DELETED.getCode().equals(user.getDelFlag())
                || UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            throw new ServiceException("UKey 绑定的系统账号已停用或删除");
        }
        LoginUser loginUser = new LoginUser(user.getUserId(), user.getDeptId(), user,
                permissionService.getMenuPermission(user));
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(user.getUserName(), Constants.LOGIN_SUCCESS, "UKey 登录成功"));
        loginService.recordLoginInfo(user.getUserId());
        return tokenService.createToken(loginUser);
    }

    public CaIdentity verifyIdentity(String challengeId, String identityTicket) {
        if (StringUtils.isBlank(challengeId) || !challengeId.matches("[0-9a-fA-F-]{36}")
                || StringUtils.isBlank(identityTicket) || identityTicket.length() > 65536) {
            throw new ServiceException("CA登录参数无效");
        }
        String key = CHALLENGE_PREFIX + challengeId;
        // SETNX 保证同一个挑战值只有一个请求进入验票流程。
        Boolean first = redisCache.redisTemplate.opsForValue()
                .setIfAbsent(key + ":used", "1", CHALLENGE_TTL_SECONDS, TimeUnit.SECONDS);
        if (!Boolean.TRUE.equals(first)) {
            throw new ServiceException("CA挑战值已使用，请重试");
        }
        String challenge = redisCache.getCacheObject(key);
        redisCache.deleteObject(key);
        if (StringUtils.isBlank(challenge)) {
            throw new ServiceException("CA挑战值已过期，请重试");
        }

        if (mockEnabled) {
            if (!identityTicket.equals("MOCK:" + challenge)) {
                throw new ServiceException("模拟 UKey 票据无效");
            }
            return new CaIdentity("MOCK-RMS-001", "MOCK-CERT-001");
        }
        checkConfiguration();
        VerifyIdTRet verification = client().verifyIdentityTicket(appServerId, challenge, identityTicket);
        return verifiedIdentity(verification, challenge);
    }

    private CaIdentity verifiedIdentity(VerifyIdTRet verification, String challenge) {
        if (verification == null || StringUtils.isBlank(verification.getResult())) {
            throw new ServiceException("CA验票没有返回结果");
        }
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);
            Document xml = factory.newDocumentBuilder().parse(new InputSource(new StringReader(verification.getResult())));
            Element root = xml.getDocumentElement();
            if (!"verifyIdentityTicketResult".equals(root.getLocalName())
                    && !"verifyIdentityTicketResult".equals(root.getNodeName())) {
                throw new ServiceException("CA验票返回格式错误");
            }
            String result = childText(root, "result");
            boolean success = false;
            for (String allowed : successResults.split(",")) {
                if (allowed.trim().equalsIgnoreCase(result)) success = true;
            }
            if (!success || !StringUtils.isBlank(childText(root, "error"))
                    || !challenge.equals(childText(root, "challenge"))) {
                throw new ServiceException("CA身份验证失败");
            }
            NodeList users = root.getElementsByTagNameNS("*", "userinfo");
            if (users.getLength() != 1) {
                throw new ServiceException("CA未返回唯一用户身份");
            }
            Element userInfo = (Element) users.item(0);
            String rmsId = childText(userInfo, "rmsid");
            String certSn = childText(userInfo, "certSN");
            if (StringUtils.isBlank(rmsId) || StringUtils.isBlank(certSn)) {
                throw new ServiceException("CA未返回 RMS 用户编号或证书序列号");
            }
            return new CaIdentity(rmsId, certSn);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.warn("CA验票结果解析失败", e);
            throw new ServiceException("CA验票返回格式错误");
        }
    }

    private String childText(Element parent, String name) {
        NodeList nodes = parent.getElementsByTagNameNS("*", name);
        if (nodes.getLength() == 0) nodes = parent.getElementsByTagName(name);
        return nodes.getLength() == 0 ? "" : nodes.item(0).getTextContent().trim();
    }

    private TokenCheckWS client() {
        try {
            TokenCheckWS port = new TokenCheckServiceService(new URL(wsdlUrl)).getTokenCheckServicePort();
            Map<String, Object> context = ((BindingProvider) port).getRequestContext();
            context.put("com.sun.xml.internal.ws.connect.timeout", 5000);
            context.put("com.sun.xml.internal.ws.request.timeout", 10000);
            context.put("com.sun.xml.ws.connect.timeout", 5000);
            context.put("com.sun.xml.ws.request.timeout", 10000);
            return port;
        } catch (Exception e) {
            log.warn("SAP2000 服务连接失败", e);
            throw new ServiceException("CA认证服务不可用");
        }
    }

    private void checkConfiguration() {
        if (!mockEnabled && (StringUtils.isBlank(wsdlUrl) || StringUtils.isBlank(appServerId)
                || StringUtils.isBlank(serverIp))) {
            throw new ServiceException("SAP2000 服务参数尚未配置");
        }
    }
}
