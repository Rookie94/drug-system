package com.ruoyi.framework.security.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.List;

import org.bouncycastle.crypto.engines.SM4Engine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.encoders.Hex;

import com.ruoyi.framework.security.handle.DecryptionRequestWrapper;
import com.ruoyi.framework.security.handle.EncryptionResponseWrapper;

/**
 * 国密SM4加解密过滤器（动态IV + CBC模式）
 */
public class DecryptionFilter implements Filter {

    private Boolean enabled;
    private String key;
    private List<String> filterUrls;
    private final SecureRandom secureRandom = new SecureRandom();

    public DecryptionFilter(Boolean enabled, String key, List<String> filterUrls) {
        if (key == null || key.length() != 32) {
            throw new IllegalArgumentException("SM4 key must be 32-character hex string (128-bit)");
        }
        this.enabled = enabled;
        this.key = key;
        this.filterUrls = filterUrls;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 跳过未启用或白名单URL
        if (!enabled || isUrlMatchFilterUrls(httpRequest.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        try {
            // 初始化SM4密钥
            byte[] keyBytes = Hex.decode(key);
            KeyParameter keyParam = new KeyParameter(keyBytes);

            // 处理请求解密
            String requestBody = getRequestBody(httpRequest);
            if (!requestBody.isEmpty()) {
                requestBody = requestBody.trim().replace("\"", "");

                // 解密格式：IV(16字节) + 密文
                byte[] encryptedData = Hex.decode(requestBody);
                if (encryptedData.length < 16) {
                    httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid encrypted data (missing IV)");
                    return;
                }

                // 提取IV和密文
                byte[] iv = new byte[16];
                System.arraycopy(encryptedData, 0, iv, 0, 16);
                byte[] ciphertext = new byte[encryptedData.length - 16];
                System.arraycopy(encryptedData, 16, ciphertext, 0, ciphertext.length);

                // 解密请求体
                String decryptedBody = decryptSM4(ciphertext, new ParametersWithIV(keyParam, iv));
                DecryptionRequestWrapper wrappedRequest = new DecryptionRequestWrapper(httpRequest, decryptedBody);
                EncryptionResponseWrapper wrappedResponse = new EncryptionResponseWrapper(httpResponse);

                chain.doFilter(wrappedRequest, wrappedResponse);

                // 加密响应
                encryptResponse(httpResponse, wrappedResponse, keyParam);
            } else {
                // 无请求体时的处理
                EncryptionResponseWrapper wrappedResponse = new EncryptionResponseWrapper(httpResponse);
                chain.doFilter(request, wrappedResponse);
                encryptResponse(httpResponse, wrappedResponse, keyParam);
            }
        } catch (Exception e) {
            throw new ServletException("SM4 processing error", e);
        }
    }

    /**
     * SM4解密（CBC模式）
     */
    private String decryptSM4(byte[] ciphertext, ParametersWithIV params) throws Exception {
        PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new SM4Engine()));
        cipher.init(false, params);

        byte[] output = new byte[cipher.getOutputSize(ciphertext.length)];
        int len = cipher.processBytes(ciphertext, 0, ciphertext.length, output, 0);
        len += cipher.doFinal(output, len);

        return new String(output, 0, len, StandardCharsets.UTF_8);
    }

    /**
     * SM4加密（CBC模式 + 动态IV）
     */
    private byte[] encryptSM4(byte[] plaintext, KeyParameter keyParam) throws Exception {
        // 生成随机IV
        byte[] iv = new byte[16];
        secureRandom.nextBytes(iv);

        PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new SM4Engine()));
        cipher.init(true, new ParametersWithIV(keyParam, iv));

        byte[] output = new byte[cipher.getOutputSize(plaintext.length)];
        int len = cipher.processBytes(plaintext, 0, plaintext.length, output, 0);
        len += cipher.doFinal(output, len);

        // 返回 IV + 密文
        byte[] result = new byte[16 + len];
        System.arraycopy(iv, 0, result, 0, 16);
        System.arraycopy(output, 0, result, 16, len);
        return result;
    }

    /**
     * 加密响应数据
     */
    private void encryptResponse(HttpServletResponse response, EncryptionResponseWrapper wrapper, KeyParameter keyParam)
            throws Exception {
        byte[] responseData = wrapper.getContentAsByteArray();
        if (responseData.length > 0) {
            byte[] encrypted = encryptSM4(responseData, keyParam);
            response.getOutputStream().write(Hex.encode(encrypted));
        }
    }

    /**
     * 读取请求体
     */
    private String getRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    /**
     * URL白名单匹配
     */
    private boolean isUrlMatchFilterUrls(String requestUrl) {
        if (filterUrls == null) return false;
        return filterUrls.stream().anyMatch(pattern ->
                requestUrl.matches(pattern.replace("*", ".*")));
    }

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void destroy() {}
}