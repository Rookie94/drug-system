package com.ruoyi.common.utils;

import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.HashMap;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.ruoyi.common.core.domain.model.WxParam;

public class WeChatUtils {

    /**
     * AES解密
     * @param sessionKey
     * @param encryptedIv
     * @param encryptedData
     */
    private static String decryptData(String sessionKey, String encryptedIv, String encryptedData) throws Exception {
        //转化为字节数组
        byte[] key = Base64.decode(sessionKey);
        byte[] iv = Base64.decode(encryptedIv);
        byte[] encData = Base64.decode(encryptedData);
        // 如果密钥不足16位，那么就补足
        int base = 16;
        if (key.length % base != 0) {
            int groups = key.length / base + (key.length % base != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(key, 0, temp, 0, key.length);
            key = temp;
        }

        // 如果初始向量不足16位，也补足
        if (iv.length % base != 0) {
            int groups = iv.length / base + (iv.length % base != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(iv, 0, temp, 0, iv.length);
            iv = temp;
        }

        AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
        String resultStr = null;

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            resultStr = new String(cipher.doFinal(encData), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //解析解密后的字符串
        return resultStr;
    }

    /**
     * 请求微信接口服务，获取openid和session_key
     *
     * @param appId
     * @param appSecret
     * @param code
     * @return
     */
    public static WxParam getOpenIdAndSessionKey(String appId, String appSecret, String code) {
        try {
            WxParam param = new WxParam();
            //向微信服务器发送请求获取用户信息
            String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appId + "&secret=" + appSecret + "&js_code=" + code + "&grant_type=authorization_code";
            RestTemplate restTemplate=new RestTemplate();
            String res = restTemplate.getForObject(url, String.class);
            JSONObject jsonObject = JSONObject.parseObject(res);
            //System.out.println("jsonObject" + jsonObject);
            //获取session_key和openid
            String sessionKey = jsonObject.getString("session_key");
            String openId = jsonObject.getString("openid");
            param.setSessionKey(sessionKey);
            param.setOpenId(openId);
            return param;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 请求微信接口服务，获取小程序全局唯一后台接口调用凭据（access_token）
     * https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/access-token/auth.getAccessToken.html
     *
     * @param appid
     * @param appSecret
     * @return
     */
    public static String getAccessToken(String appid, String appSecret) {
        String result = "";
        try {
            String baseUrl = "https://api.weixin.qq.com/cgi-bin/token";
            HashMap<String, Object> requestParam = new HashMap<>();
            // 小程序 appId
            requestParam.put("grant_type", "client_credential");
            // 小程序唯一凭证id
            requestParam.put("appid", appid);
            // 小程序 appSecret（小程序的唯一凭证密钥）
            requestParam.put("secret", appSecret);
            // 发送GET请求读取调用微信接口获取openid用户唯一标识
            String result1 = HttpUtil.get(baseUrl, requestParam);
            JSONObject jsonObject=JSONObject.parseObject(result);
            result=jsonObject.getString("access_token");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static WxParam parseWxParam(WxParam wxParam,String sessionKey,String phoneCode,String encryptedIv,String encryptedData){
        try {
            String decryptResult = decryptData(sessionKey,encryptedIv,encryptedData);
            //字符串转json
            JSONObject jsonObject = JSONObject.parseObject(decryptResult);
            //获取openid,这里本可以获取unionid的
            //String unionid = jsonObject.getString("unionid");
            //获取nickName
            String nickName = jsonObject.getString("nickName");
            //获取头像
            String avatarUrl = jsonObject.getString("avatarUrl");
            String phoneNumber = jsonObject.getString("phoneNumber");
            String purePhoneNumber = jsonObject.getString("purePhoneNumber");
            String countryCode = jsonObject.getString("countryCode");
            wxParam.setAvatarUrl(avatarUrl);
            wxParam.setPhoneNumber(phoneNumber);
            wxParam.setPurePhoneNumber(purePhoneNumber);
            wxParam.setCountryCode(countryCode);
            return wxParam;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
