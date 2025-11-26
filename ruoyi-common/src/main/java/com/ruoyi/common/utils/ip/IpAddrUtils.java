package com.ruoyi.common.utils.ip;

import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;

import java.io.InputStream;
import java.util.regex.Pattern;

import org.springframework.util.FileCopyUtils;

/**
 * ip2region 工具
 */
@Slf4j
public final class IpAddrUtils {

    /** 未知地址常量 */
    public static final String UNKNOWN = "XX XX";

    /** 内网提示 */
    private static final String LOCAL_IP = "内网IP";

    /** IP 格式校验 */
    private static final Pattern IP_PATTERN = Pattern.compile(
            "^((1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.){3}(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$");

    private IpAddrUtils() {

    }

    /** 静态内部类：负责一次性加载 Searcher */
    private static class Holder {
        private static final Searcher SEARCHER = createSearcher();
    }

    /** 外部入口：获取共享 Searcher */
    private static Searcher searcher() {
        return Holder.SEARCHER;
    }

    /** 创建 Searcher：只执行一次 */
    private static Searcher createSearcher() {
        try (InputStream is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("location/ip2region.xdb")) {
            if (is == null) {
                throw new IllegalStateException("location/ip2region.xdb 不存在");
            }
            byte[] buf = FileCopyUtils.copyToByteArray(is);
            return Searcher.newWithBuffer(buf);   // 内存模式，并发安全
        } catch (Exception e) {
            log.error("ip2region 初始化失败", e);
            throw new RuntimeException(e);
        }
    }

    /** 查询入口 */
    public static String getAddrByIP(String ip) {
        if (!IP_PATTERN.matcher(ip).matches()) {
            return UNKNOWN;
        }
        if (IpUtils.internalIp(ip)) {   // 你的内网判断工具
            return LOCAL_IP;
        }
        try {
            return searcher().search(ip);
        } catch (Exception e) {
            log.warn("ip2region 查询失败: {}", ip, e);
            return UNKNOWN;
        }
    }

    public static String getRealAddressByIP(String ip) {
        String raw = getAddrByIP(ip);
        if (raw == null || raw.equals(UNKNOWN) || raw.equals(LOCAL_IP)) {
            return raw;
        }
        String[] arr = raw.split("\\|");
        if (arr.length < 4) {          // 数据异常
            return UNKNOWN;
        }
        String province = arr[2];
        String city     = arr[3];
        // 去掉“0”“未知”等占位
        if ("0".equals(province)) province = "";
        if ("0".equals(city))     city = "";
        return (province + " " + city).trim().replaceAll("\\s+", " ");
    }

}