package com.ruoyi.framework.config;

import com.ruoyi.framework.security.provider.SmsCodeAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.filter.CorsFilter;
import com.ruoyi.framework.config.properties.PermitAllUrlProperties;
import com.ruoyi.framework.security.filter.JwtAuthenticationTokenFilter;
import com.ruoyi.framework.security.handle.AuthenticationEntryPointImpl;
import com.ruoyi.framework.security.handle.LogoutSuccessHandlerImpl;

import java.util.List;

/**
 * Spring Security 配置（Spring Security 5.7+ 无 WebSecurityConfigurerAdapter 写法）
 */
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationEntryPointImpl unauthorizedHandler;

    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;

    @Autowired
    private JwtAuthenticationTokenFilter authenticationTokenFilter;

    @Autowired
    private CorsFilter corsFilter;

    @Autowired
    private PermitAllUrlProperties permitAllUrl;

    /**
     * 唯一 AuthenticationManager：把 Dao 和 Sms 两种 Provider 都注册进去
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(userDetailsService);
        daoProvider.setPasswordEncoder(bCryptPasswordEncoder());

        SmsCodeAuthenticationProvider smsProvider = smsCodeAuthenticationProvider();

        return new ProviderManager(java.util.Arrays.asList(daoProvider, smsProvider));
    }

    @Bean
    public SmsCodeAuthenticationProvider smsCodeAuthenticationProvider() {
        return new SmsCodeAuthenticationProvider();
    }

    /**
     * SecurityFilterChain 定义：放行规则、过滤器顺序
     */
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .headers(h -> h
                        .frameOptions(f -> f.sameOrigin())
                        .cacheControl(c -> c.disable()))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> {
                    permitAllUrl.getUrls().forEach(url -> req.antMatchers(url).permitAll());
                    req.antMatchers("/login", "/ca/challenge", "/ca/login", "/register", "/captchaImage","/getarea",
                                    "/sendSms", "/smsLogin", "/wxLogin","/R2F7juVgcS.txt",
                                    "/check-binding", "/binding-phone","/api/res/**",
                                    "/captcha/**").permitAll()
                            //.antMatchers(HttpMethod.GET, "/", "/*.html", "/**/*.html","/**/*.css", "/**/*.js", "/profile/**").permitAll()
                            //.antMatchers("/swagger-ui.html", "/swagger-resources/**","/webjars/**", "/*/api-docs", "/druid/**").permitAll()
                            .anyRequest().authenticated();
                })
                .logout(lo -> lo.logoutUrl("/logout")
                        .logoutSuccessHandler(logoutSuccessHandler))
                // 修正过滤器顺序
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * 密码加密器
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
