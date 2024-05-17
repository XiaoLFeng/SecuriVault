package com.xlf.securivault.exceptions.library;

import lombok.Getter;

/**
 * 系统参数错误异常
 * <hr/>
 * 当系统参数错误时，抛出此异常, 用于处理系统参数错误
 *
 * @author xiao_lfeng
 * @version 1.0.0
 * @see com.xlf.securivault.exceptions.PublicException
 * @since 1.0.0
 */
@Getter
public class SystemParameterError extends RuntimeException {
    /**
     * 附加数据
     */
    private final Object data;

    /**
     * 构造函数
     * <hr/>
     * 构造一个系统参数错误异常, 用于处理系统参数错误
     *
     * @param message 异常信息
     * @param data    附加数据
     */
    public SystemParameterError(String message, Object data) {
        super(message);
        this.data = data;
    }
}
