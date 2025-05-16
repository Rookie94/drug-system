package com.ruoyi.framework.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.DispatcherType;

import com.ruoyi.framework.security.filter.DecryptionFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.ruoyi.common.filter.RepeatableFilter;
import com.ruoyi.common.filter.XssFilter;
import com.ruoyi.common.utils.StringUtils;

/**
 * Filter配置
 *
 * @author ruoyi
 */
@Configuration
public class FilterConfig
{
    @Value("${xss.excludes}")
    private String excludes;

    @Value("${xss.urlPatterns}")
    private String urlPatterns;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    @ConditionalOnProperty(value = "xss.enabled", havingValue = "true")
    public FilterRegistrationBean xssFilterRegistration()
    {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new XssFilter());
        registration.addUrlPatterns(StringUtils.split(urlPatterns, ","));
        registration.setName("xssFilter");
        registration.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE);
        Map<String, String> initParameters = new HashMap<String, String>();
        initParameters.put("excludes", excludes);
        registration.setInitParameters(initParameters);
        return registration;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    public FilterRegistrationBean someFilterRegistration()
    {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new RepeatableFilter());
        registration.addUrlPatterns("/*");
        registration.setName("repeatableFilter");
        registration.setOrder(FilterRegistrationBean.LOWEST_PRECEDENCE);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<DecryptionFilter> decryptionFilter(
            @Value("${encrypt.enabled}") Boolean enabled,
            @Value("${encrypt.key}") String key,
            @Value("${encrypt.filterUrls}") List<String> filterUrls) {
        FilterRegistrationBean<DecryptionFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE + 1);
        DecryptionFilter decryptionFilter = new DecryptionFilter(enabled, key, filterUrls);
        registrationBean.setFilter(decryptionFilter);
        registrationBean.addUrlPatterns("/*"); // 根据需要调整URL模式
        return registrationBean;
    }


}
