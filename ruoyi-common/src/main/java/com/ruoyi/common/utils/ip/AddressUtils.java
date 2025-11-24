package com.ruoyi.common.utils.ip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;

import javax.annotation.PreDestroy;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 获取地址类
 *
 * @author ruoyi
 */
public class AddressUtils
{
    private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);

    // IP地址查询
    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";

    // 未知地址
    public static final String UNKNOWN = "XX XX";

    private static final AtomicReference<Object> SEARCHER_REF = new AtomicReference<>();
    private static volatile boolean ip2regionEnabled = false;
    private static volatile boolean initAttempted = false;

    /**
     * 初始化 IP 查询器（懒加载）
     */
    private static void initSearcherIfNeeded() {
        if (initAttempted) {
            return;
        }

        synchronized (AddressUtils.class) {
            if (initAttempted) {
                return;
            }

            try {
                // 检查 ip2region 类是否存在
                Class.forName("org.lionsoul.ip2region.xdb.Searcher");
                initSearcher();
                ip2regionEnabled = true;
                log.info("ip2region 初始化成功");
            } catch (ClassNotFoundException e) {
                log.warn("ip2region 库未找到，将使用在线 IP 查询服务");
                ip2regionEnabled = false;
            } catch (Throwable t) {
                log.error("ip2region 初始化失败，将使用在线查询", t);
                ip2regionEnabled = false;
            } finally {
                initAttempted = true;
            }
        }
    }

    private static void initSearcher() throws IOException, ReflectiveOperationException {
        String file = "ip2region_v4.xdb";
        try (InputStream in = AddressUtils.class.getClassLoader().getResourceAsStream(file)) {
            if (in == null) {
                throw new FileNotFoundException("IP 数据库文件未找到: " + file);
            }
            byte[] bytes = toByteArray(in);

            // 使用反射创建 Searcher 实例，避免编译期依赖问题
            Class<?> searcherClass = Class.forName("org.lionsoul.ip2region.xdb.Searcher");
            Object searcher = searcherClass.getMethod("newWithBuffer", byte[].class)
                    .invoke(null, bytes);
            SEARCHER_REF.set(searcher);
            log.info("ip2region 搜索器初始化成功，数据大小: {} bytes", bytes.length);
        }
    }

    /* 兼容 JDK 8 的工具方法 */
    private static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int n;
        while ((n = in.read(buf)) != -1) {
            out.write(buf, 0, n);
        }
        return out.toByteArray();
    }

    public static String getRealAddressByIP(String ip) {
        if (IpUtils.internalIp(ip)) {
            return "内网IP";
        }

        // 尝试使用本地 IP 库查询
        if (tryLocalIpQuery(ip)) {
            String result = getRealAddressByLocal(ip);
            if (!UNKNOWN.equals(result)) {
                return result;
            }
        }

        // 回退到在线查询
        return getRealAddressByOnline(ip);
    }

    /**
     * 尝试使用本地 IP 库查询
     */
    private static boolean tryLocalIpQuery(String ip) {
        if (!initAttempted) {
            initSearcherIfNeeded();
        }
        return ip2regionEnabled && SEARCHER_REF.get() != null;
    }

    /**
     * 使用本地 IP 库查询
     */
    private static String getRealAddressByLocal(String ip) {
        try {
            Object searcher = SEARCHER_REF.get();
            if (searcher == null) {
                return UNKNOWN;
            }

            // 使用反射调用搜索方法
            String region = (String) searcher.getClass()
                    .getMethod("search", String.class)
                    .invoke(searcher, ip);

            if (StringUtils.isEmpty(region)) {
                return UNKNOWN;
            }

            /* region 格式: 国家|区域|省份|城市|ISP */
            String[] arr = region.split("\\|", -1);
            if (arr.length < 5) {
                return UNKNOWN;
            }

            String country = arr[0];
            String province = arr[2];
            String city = arr[3];
            String isp = arr[4];

            // 过滤无效信息
            if ("0".equals(province) || StringUtils.isEmpty(province)) {
                return UNKNOWN;
            }

            // 构建地址信息
            StringBuilder address = new StringBuilder();
            if (!"0".equals(province) && !StringUtils.isEmpty(province)) {
                address.append(province);
            }
            if (!"0".equals(city) && !StringUtils.isEmpty(city) && !city.equals(province)) {
                if (address.length() > 0) {
                    address.append(" ");
                }
                address.append(city);
            }

            return address.length() > 0 ? address.toString() : UNKNOWN;

        } catch (Exception e) {
            log.warn("本地 IP 查询失败: {}, 将使用在线查询", ip, e);
            return UNKNOWN;
        }
    }

    /**
     * 使用在线服务查询 IP 地址
     */
    public static String getRealAddressByOnline(String ip) {
        if (!RuoYiConfig.isAddressEnabled()) {
            return UNKNOWN;
        }

        try {
            String rspStr = HttpUtils.sendGet(IP_URL, "ip=" + ip + "&json=true", Constants.GBK);
            if (StringUtils.isEmpty(rspStr)) {
                log.error("获取地理位置异常 {}", ip);
                return UNKNOWN;
            }
            JSONObject obj = JSON.parseObject(rspStr);
            String region = obj.getString("pro");
            String city = obj.getString("city");
            return String.format("%s %s",
                    StringUtils.isEmpty(region) ? "" : region,
                    StringUtils.isEmpty(city) ? "" : city);
        } catch (Exception e) {
            log.error("在线获取地理位置异常 {}", ip, e);
            return UNKNOWN;
        }
    }

    /* 优雅关闭 */
    @PreDestroy
    public static void destroy() {
        Object searcher = SEARCHER_REF.getAndSet(null);
        if (searcher != null) {
            try {
                searcher.getClass().getMethod("close").invoke(searcher);
                log.info("ip2region 搜索器已关闭");
            } catch (Exception e) {
                log.warn("关闭 ip2region 搜索器时发生异常", e);
            }
        }
        ip2regionEnabled = false;
        initAttempted = false;
    }
}