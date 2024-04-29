package com.xlf.securivault.config.configuration;

import com.xlf.securivault.config.filter.RequestHeaderFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 安全配置
 * <hr/>
 * 用于配置安全相关的配置信息，包括安全拦截器、安全过滤器等；
 *
 * @since v1.0.0
 * @version v1.0.0
 * @author xiao_lfeng
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 安全过滤器
     * <hr/>
     * 用于配置安全过滤器，用于配置安全过滤器的相关信息
     *
     * @param security 安全，用于配置安全相关的信息
     * @return 安全过滤器
     * @throws Exception 异常
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        security
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilter(new RequestHeaderFilter())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll()
                        .anyRequest().permitAll()
                );
        return security.build();
    }

}
