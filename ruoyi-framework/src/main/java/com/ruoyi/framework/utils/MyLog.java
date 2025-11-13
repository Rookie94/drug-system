package com.ruoyi.framework.utils;

import com.ruoyi.common.utils.ip.AddressUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.SysCustomLog;
import com.ruoyi.system.service.ISysCustomLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

/**
 * 自定义日志工具类 - 精简版
 */
public class MyLog {

    private static final Logger log = LoggerFactory.getLogger(MyLog.class);

    private static final ISysCustomLogService logService = SpringUtils.getBean(ISysCustomLogService.class);

    /**
     * 记录操作日志
     */
    public static void record(String title, String businessModule, String status, String logDetail) {
        record(title, businessModule, null, status, logDetail, null);
    }

    /**
     * 记录操作日志（带业务ID）
     */
    public static void record(String title, String businessModule, String businessId, String status, String logDetail) {
        record(title, businessModule, businessId, status, logDetail, null);
    }

    /**
     * 记录操作日志（带执行时间）
     */
    public static void record(String title, String businessModule, String businessId, String status,
                              String logDetail, Long executeTime) {
        try {
            SysCustomLog customLog = buildLog(title, businessModule, businessId, status, logDetail, executeTime);
            logService.insertLog(customLog);
        } catch (Exception e) {
            log.error("记录自定义日志失败: {}", title, e);
        }
    }

    /**
     * 异步记录日志
     */
    public static CompletableFuture<Void> recordAsync(String title, String businessModule, String status, String logDetail) {
        try {
            SysCustomLog customLog = buildLog(title, businessModule, null, status, logDetail, null);
            return logService.asyncInsertLog(customLog);
        } catch (Exception e) {
            log.error("异步记录自定义日志失败: {}", title, e);
            return CompletableFuture.completedFuture(null);
        }
    }

    /**
     * 记录成功日志
     */
    public static void success(String title, String businessModule, String logDetail) {
        record(title, businessModule, null, "0", logDetail, null);
    }

    /**
     * 记录失败日志
     */
    public static void error(String title, String businessModule, String errorMsg, String logDetail) {
        try {
            SysCustomLog customLog = buildLog(title, businessModule, null, "1", logDetail, null);
            customLog.setErrorMsg(errorMsg);
            logService.insertLog(customLog);
        } catch (Exception e) {
            log.error("记录错误日志失败: {}", title, e);
        }
    }

    /**
     * 记录API日志
     */
    public static void api(String title, String requestUri, String requestMethod,
                           String requestParams, String responseResult, Long executeTime, String status) {
        try {
            SysCustomLog customLog = buildLog(title, "API接口", null, status, null, executeTime);
            customLog.setRequestUri(requestUri);
            customLog.setRequestMethod(requestMethod);
            customLog.setRequestParams(requestParams);
            customLog.setResponseResult(responseResult);
            logService.insertLog(customLog);
        } catch (Exception e) {
            log.error("记录API日志失败: {}", title, e);
        }
    }

    /**
     * 构建日志对象
     */
    private static SysCustomLog buildLog(String title, String businessModule, String businessId,
                                         String status, String logDetail, Long executeTime) {
        SysCustomLog customLog = new SysCustomLog();
        customLog.setLogTitle(title);
        customLog.setBusinessModule(businessModule);
        customLog.setBusinessId(businessId);
        customLog.setOperName(getCurrentUsername());
        customLog.setStatus(status);
        customLog.setLogDetail(logDetail);
        customLog.setExecuteTime(executeTime);
        customLog.setOperTime(new Date());

        // 设置IP和位置信息
        setIpAndLocation(customLog);

        return customLog;
    }

    /**
     * 设置IP和位置信息
     */
    private static void setIpAndLocation(SysCustomLog customLog) {
        try {
            HttpServletRequest request = getRequest();
            if (request != null) {
                String ip = IpUtils.getIpAddr(request);
                customLog.setOperIp(ip);
                customLog.setOperLocation(AddressUtils.getRealAddressByIP(ip));
            }
        } catch (Exception e) {
            log.warn("获取IP和位置信息失败", e);
        }
    }

    /**
     * 获取当前请求
     */
    private static HttpServletRequest getRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return attributes != null ? attributes.getRequest() : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前用户名
     */
    private static String getCurrentUsername() {
        try {
            // 这里需要根据您的认证系统获取当前用户
            // 例如：return SecurityUtils.getUsername();
            return "system";
        } catch (Exception e) {
            return "unknown";
        }
    }
}