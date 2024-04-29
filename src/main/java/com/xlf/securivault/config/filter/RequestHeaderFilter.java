package com.xlf.securivault.config.filter;

import com.xlf.securivault.exceptions.library.RequestHeaderNotMatchException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * 请求头过滤器
 * <hr/>
 * 用于过滤请求头，对请求头进行处理；
 *
 * @version 1.0.0
 * @since 1.0.0
 * @see Filter
 * @see com.xlf.securivault.config.configuration.SecurityConfig
 * @author xiao_lfeng
 */
public class RequestHeaderFilter implements Filter {

    @Override
    public void doFilter(
            @NotNull ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        // 检查请求头是否为 application/json
        if (!"application/json".equals(req.getContentType())) {
            throw new RequestHeaderNotMatchException("请求 application/json 类型错误");
        }
        // 检查请求头是否包含正确的 User-Agent
        if (req.getHeader("User-Agent") == null || req.getHeader("User-Agent").isEmpty()){
            throw new RequestHeaderNotMatchException("请求头中缺少 User-Agent");
        }
        chain.doFilter(request, response);
    }
}
