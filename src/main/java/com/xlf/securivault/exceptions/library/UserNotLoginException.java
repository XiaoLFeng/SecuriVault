package com.xlf.securivault.exceptions.library;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;

/**
 * 用户未登录异常
 * <hr/>
 * 当用户未登录时，抛出此异常, 用于处理用户未登录的情况
 *
 * @author xiao_lfeng
 * @version 1.0.0
 * @see com.xlf.securivault.exceptions.PublicException
 * @since 1.0.0
 */
@Getter
public class UserNotLoginException extends RuntimeException {
    /**
     * 用户 IP
     */
    private final String userIp;
    /**
     * 用户代理
     */
    private final String userAgent;
    /**
     * 请求 URL
     */
    private final String requestUrl;
    /**
     * 请求方法
     */
    private final String requestMethod;
    /**
     * 用户令牌
     */
    private final String userToken;
    /**
     * 用户 UUID
     */
    private final String userUUID;

    /**
     * 构造函数
     * <hr/>
     * 构造一个用户未登录异常, 用于处理用户未登录的情况, 需要指定异常信息，异常信息将会被打印到日志中以及返回前端部分在 errorMessage 中
     *
     * @param message 异常信息
     * @param request 请求，用于获取该用户请求的一些相关信息
     */
    public UserNotLoginException(String message, @Nullable HttpServletRequest request) {
        super(message);
        if (request == null) {
            this.userIp = null;
            this.userAgent = null;
            this.requestUrl = null;
            this.requestMethod = null;
            this.userToken = null;
            this.userUUID = null;
        } else {
            this.userIp = request.getRemoteAddr();
            this.userAgent = request.getHeader("User-Agent");
            this.requestUrl = request.getRequestURI();
            this.requestMethod = request.getMethod();
            this.userToken = request.getHeader("Authorization");
            this.userUUID = request.getHeader("X-User-UUID");
        }
    }
}
