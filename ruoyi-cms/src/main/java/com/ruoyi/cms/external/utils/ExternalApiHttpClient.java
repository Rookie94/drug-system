package com.ruoyi.cms.external.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.cms.external.config.ExternalApiConfig;
import com.ruoyi.cms.external.domain.CarePerson;
import com.ruoyi.cms.external.domain.Police;
import com.ruoyi.cms.external.domain.response.ExternalApiResponse;
import com.ruoyi.cms.external.domain.response.ExternalPageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

/**
 * API HTTP客户端工具类
 */
@Component
public class ExternalApiHttpClient {

    private static final Logger log = LoggerFactory.getLogger(ExternalApiHttpClient.class);

    @Autowired
    private ExternalApiConfig apiConfig;

    @Autowired
    private ObjectMapper objectMapper;

    private final RestTemplate restTemplate;

    public ExternalApiHttpClient() {
        this.restTemplate = createRestTemplate();
    }

    private RestTemplate createRestTemplate() {
        // 配置HTTP客户端
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(30000)
                .setConnectTimeout(30000)
                .setSocketTimeout(300000)
                .build();

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);

        RestTemplate template = new RestTemplate(factory);

        // 设置消息转换器
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
        template.setMessageConverters(converters);

        return template;
    }

    /**
     * 生成签名 - 根据接口类型使用不同的签名规则
     */
    public String generateSign(String interfaceName, String paramValue) {
        try {
            String interfaceNameLower = interfaceName.toLowerCase();
            String signStr;

            // 验证接口名称是否有效
            if (!apiConfig.isValidInterface(interfaceNameLower)) {
                throw new IllegalArgumentException("未知的接口类型: " + interfaceName);
            }

            // 根据接口类型选择签名规则
            if (apiConfig.getParamSignInterfaces().contains(interfaceNameLower)) {
                // 需要参数值的签名：秘钥 + 接口名称（小写）+ 提交的参数值
                signStr = apiConfig.getSecretKey() + interfaceNameLower + paramValue;
                log.debug("使用带参数签名规则: 接口={}, 参数值={}", interfaceNameLower, paramValue);
            } else {
                // 不需要参数值的签名：秘钥 + 接口名称（小写）
                signStr = apiConfig.getSecretKey() + interfaceNameLower;
                log.debug("使用无参数签名规则: 接口={}", interfaceNameLower);
            }

            log.debug("签名原始字符串: {}", signStr);

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(signStr.getBytes(StandardCharsets.UTF_8));
            String signature = bytesToHex(hash);

            log.debug("生成的签名: {}", signature);
            return signature;
        } catch (Exception e) {
            log.error("生成签名失败 - 接口: {}, 参数: {}", interfaceName, paramValue, e);
            throw new RuntimeException("生成签名失败", e);
        }
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Base64解码
     */
    private String base64Decode(String encodedStr) {
        if (StrUtil.isBlank(encodedStr)) {
            return "[]";
        }
        try {
            // Hutool的Base64解码方法
            return StrUtil.str(
                    cn.hutool.core.codec.Base64.decode(encodedStr),
                    StandardCharsets.UTF_8
            );
        } catch (Exception e) {
            log.error("Base64解码失败: {}", encodedStr, e);
            return "[]";
        }
    }

    /**
     * 发送GET请求
     */
    public <T> ExternalApiResponse<T> doGet(String interfaceName, Map<String, Object> params, Class<T> responseType) {
        try {
            String interfacePath = getInterfacePath(interfaceName);
            String url = apiConfig.getBaseUrl() + interfacePath;

            // 构建查询参数和URL
            String paramValue = "";
            if (params != null && !params.isEmpty()) {
                StringBuilder queryString = new StringBuilder();
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    if (queryString.length() > 0) {
                        queryString.append("&");
                    }
                    queryString.append(entry.getKey()).append("=").append(entry.getValue());
                }
                url += "?" + queryString.toString();

                // 对于需要参数值的接口，提取参数值用于签名
                if (apiConfig.getParamSignInterfaces().contains(interfaceName.toLowerCase())) {
                    paramValue = extractParamValueFromMap(params);
                }
            }

            // 生成签名
            String sign = generateSign(interfaceName, paramValue);

            HttpHeaders headers = new HttpHeaders();
            headers.set("sign", sign);
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            log.info("发送GET请求 - 接口: {}, URL: {}, 参数: {}", interfaceName, url, params);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            log.debug("GET响应 - 接口: {}, 状态: {}, 响应体: {}",
                    interfaceName, response.getStatusCode(), response.getBody());

            return parseResponseSafely(response.getBody(), responseType, interfaceName);
        } catch (Exception e) {
            log.error("GET请求失败 - 接口: {}, 参数: {}", interfaceName, params, e);
            ExternalApiResponse<T> errorResponse = new ExternalApiResponse<>();
            errorResponse.setCode("1");
            errorResponse.setMsg("请求失败: " + e.getMessage());
            return errorResponse;
        }
    }

    /**
     * 发送POST请求
     */
    public <T> ExternalApiResponse<T> doPost(String interfaceName, Object requestBody, Class<T> responseType) {
        try {
            String interfacePath = getInterfacePath(interfaceName);
            String url = apiConfig.getBaseUrl() + interfacePath;

            // 提取参数值用于签名
            String paramValue = "";
            if (apiConfig.getParamSignInterfaces().contains(interfaceName.toLowerCase())) {
                paramValue = extractParamValue(requestBody);
            }

            // 生成签名
            String sign = generateSign(interfaceName, paramValue);

            HttpHeaders headers = new HttpHeaders();
            headers.set("sign", sign);
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            log.info("发送POST请求 - 接口: {}, URL: {}, 请求体: {}", interfaceName, url, requestBody);

            HttpEntity<Object> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            log.debug("POST响应 - 接口: {}, 状态: {}, 响应体: {}",
                    interfaceName, response.getStatusCode(), response.getBody());

            return parseResponseSafely(response.getBody(), responseType, interfaceName);
        } catch (Exception e) {
            log.error("POST请求失败 - 接口: {}", interfaceName, e);
            ExternalApiResponse<T> errorResponse = new ExternalApiResponse<>();
            errorResponse.setCode("1");
            errorResponse.setMsg("请求失败: " + e.getMessage());
            return errorResponse;
        }
    }


    /**
     * 类型安全的列表处理方法
     */
    @SuppressWarnings("unchecked")
    public <T> ExternalApiResponse<List<T>> doPostForList(String interfaceName, Object requestBody, Class<T> elementType) {
        // 根据接口类型选择正确的解析方式
        ExternalApiResponse<?> rawResponse;

        if ("police-verify".equals(interfaceName)) {
            rawResponse = doPost(interfaceName, requestBody, Police.class);
        } else if ("archives-verify".equals(interfaceName)) {
            rawResponse = doPost(interfaceName, requestBody, CarePerson.class);
        } else {
            rawResponse = doPost(interfaceName, requestBody, Object.class);
        }

        ExternalApiResponse<List<T>> listResponse = new ExternalApiResponse<>();
        listResponse.setCode(rawResponse.getCode());
        listResponse.setMsg(rawResponse.getMsg());
        listResponse.setData(rawResponse.getData());

        Object decodedData = rawResponse.getDecodedData();
        if (decodedData instanceof List) {
            // 安全地进行类型转换
            List<?> rawList = (List<?>) decodedData;
            List<T> typedList = new ArrayList<>();
            for (Object item : rawList) {
                if (elementType.isInstance(item)) {
                    typedList.add((T) item);
                }
            }
            listResponse.setDecodedData(typedList);
        } else if (decodedData != null && elementType.isInstance(decodedData)) {
            // 如果是单个对象，包装成列表
            List<T> singleItemList = new ArrayList<>();
            singleItemList.add((T) decodedData);
            listResponse.setDecodedData(singleItemList);
        }

        return listResponse;
    }

    /**
     * 专门处理返回列表的GET请求
     */
    @SuppressWarnings("unchecked")
    public <T> ExternalApiResponse<List<T>> doGetForList(String interfaceName, Map<String, Object> params, Class<T> elementType) {
        ExternalApiResponse<T> rawResponse = doGet(interfaceName, params, elementType);

        ExternalApiResponse<List<T>> listResponse = new ExternalApiResponse<>();
        listResponse.setCode(rawResponse.getCode());
        listResponse.setMsg(rawResponse.getMsg());
        listResponse.setData(rawResponse.getData());

        if (rawResponse.getDecodedData() instanceof List) {
            listResponse.setDecodedData((List<T>) rawResponse.getDecodedData());
        } else if (rawResponse.getDecodedData() != null) {
            // 如果是单个对象，包装成列表
            List<T> singleItemList = new ArrayList<>();
            singleItemList.add(rawResponse.getDecodedData());
            listResponse.setDecodedData(singleItemList);
        }

        return listResponse;
    }

    /**
     * 获取接口路径
     */
    private String getInterfacePath(String interfaceName) {
        Map<String, String> interfaces = apiConfig.getInterfaces();
        String path = interfaces.get(interfaceName);
        if (path == null) {
            throw new IllegalArgumentException("未知的接口名称: " + interfaceName);
        }
        return path;
    }

    /**
     * 从Map中提取参数值用于签名
     */
    private String extractParamValueFromMap(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        // 按照文档示例，取第一个参数的值
        return params.values().stream().findFirst().map(Object::toString).orElse("");
    }

    /**
     * 从请求体中提取参数值用于签名
     */
    private String extractParamValue(Object requestBody) {
        if (requestBody == null) {
            return "";
        }
        try {
            if (requestBody instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) requestBody;
                return extractParamValueFromMap((Map<String, Object>) map);
            } else {
                Map<String, Object> map = objectMapper.convertValue(requestBody, Map.class);
                return extractParamValueFromMap(map);
            }
        } catch (Exception e) {
            log.warn("提取参数值失败，使用空字符串", e);
            return "";
        }
    }

    /**
     * 类型安全的响应解析 - 使用 TypeReference 避免泛型擦除问题
     */
    private <T> ExternalApiResponse<T> parseResponseSafely(String responseBody, Class<T> responseType, String interfaceName) {
        try {
            if (StrUtil.isBlank(responseBody)) {
                throw new RuntimeException("响应体为空");
            }

            // 1. 使用Hutool解析基础ApiResponse
            cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(responseBody);
            ExternalApiResponse<T> apiResponse = new ExternalApiResponse<>();
            apiResponse.setCode(jsonObject.getStr("code"));
            apiResponse.setMsg(jsonObject.getStr("msg"));
            apiResponse.setData(jsonObject.getStr("data"));

            if (apiResponse.isSuccess() && StrUtil.isNotBlank(apiResponse.getData())) {
                String decodedData = base64Decode(apiResponse.getData());
                log.debug("接口 {} 解码后的数据: {}", interfaceName, decodedData);

                // 2. 根据接口类型处理解码后的数据
                switch (interfaceName) {
                    case "get-org-data":
                        // 单个对象解析
                        T decodedObj = JSONUtil.toBean(decodedData, responseType);
                        apiResponse.setDecodedData(decodedObj);
                        break;

                    case "all-archives-info":
                        // 分页响应处理
                        handlePageResponse(decodedData, apiResponse);
                        break;

                    default:
                        // 其他接口（返回数组的接口）
                        handleArrayResponse(decodedData, apiResponse, responseType, interfaceName);
                        break;
                }
            }

            return apiResponse;
        } catch (Exception e) {
            log.error("解析响应失败，接口: {}, 响应体: {}", interfaceName, responseBody, e);
            ExternalApiResponse<T> errorResponse = new ExternalApiResponse<>();
            errorResponse.setCode("1");
            errorResponse.setMsg("解析响应失败: " + e.getMessage());
            return errorResponse;
        }
    }

    /**
     * 处理分页响应（针对all-archives-info接口）
     */
    @SuppressWarnings("unchecked")
    private <T> void handlePageResponse(String decodedData, ExternalApiResponse<T> apiResponse) {
        try {
            // 解析分页结构
            cn.hutool.json.JSONObject pageJson = JSONUtil.parseObj(decodedData);
            ExternalPageResponse<CarePerson> pageResponse = new ExternalPageResponse<>();
            pageResponse.setTotal(pageJson.getInt("total"));
            pageResponse.setPageIndex(pageJson.getInt("pageIndex"));
            pageResponse.setPageSize(pageJson.getInt("pageSize"));
            pageResponse.setList(pageJson.getStr("list"));

            // 解码list字段中的Base64数据
            if (StrUtil.isNotBlank(pageResponse.getList())) {
                String decodedList = base64Decode(pageResponse.getList());
                List<CarePerson> carePersonList = JSONUtil.toList(decodedList, CarePerson.class);
                pageResponse.setDecodedList(carePersonList);
            }

            apiResponse.setDecodedData((T) pageResponse);
        } catch (Exception e) {
            log.error("解析分页响应失败: {}", decodedData, e);
            throw new RuntimeException("分页数据解析失败", e);
        }
    }

    /**
     * 处理数组响应（针对返回数组的接口）
     */
    @SuppressWarnings("unchecked")
    private <T> void handleArrayResponse(String decodedData, ExternalApiResponse<T> apiResponse,
                                         Class<T> responseType, String interfaceName) {
        try {
            // 使用TypeReference解决复杂泛型类型问题
            if (Police.class.equals(responseType)) {
                List<Police> policeList = JSONUtil.toList(decodedData, Police.class);
                apiResponse.setDecodedData((T) policeList);
            } else if (CarePerson.class.equals(responseType)) {
                List<CarePerson> carePersonList = JSONUtil.toList(decodedData, CarePerson.class);
                apiResponse.setDecodedData((T) carePersonList);
            } else {
                // 通用数组处理
                List<T> list = JSONUtil.toList(decodedData, responseType);
                apiResponse.setDecodedData((T) list);
            }
        } catch (Exception e) {
            log.error("解析数组响应失败，接口: {}, 数据: {}", interfaceName, decodedData, e);
            throw new RuntimeException("数组数据解析失败", e);
        }
    }

}