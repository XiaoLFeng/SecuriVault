package com.xlf.securivault.exceptions.library;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * 页面未找到异常
 * <hr/>
 * 当页面未找到时，抛出此异常, 用于处理页面未找到的情况
 *
 * @version 1.0.0
 * @since 1.0.0
 * @see com.xlf.securivault.exceptions.PublicException
 * @author xiao_lfeng
 */
@Setter
@Getter
public class PageNotFoundedException extends RuntimeException {

    /**
     * 路由, 用于记录页面未找到的路由信息
     */
    private String route;

    /**
     * 构造函数
     * <hr/>
     * 构造一个页面未找到异常, 用于处理页面未找到的情况, 需要指定异常信息，异常信息将会被打印到日志中以及返回前端部分在 errorMessage 中
     *
     * @param message 异常信息
     */
    public PageNotFoundedException(String message, HttpServletRequest request) {
        super(message);
        // 获取路由信息
        this.route = request.getRequestURI();
    }

    /**
     * 构造函数
     * <hr/>
     * 构造一个页面未找到异常, 用于处理页面未找到的情况, 默认异常信息为 "页面未找到"
     */
    public PageNotFoundedException(String route) {
        super("页面未找到");
        this.route = route;
    }
}
