package com.xlf.securivault.exceptions.library;

/**
 * 请求头不匹配异常
 * <hr/>
 * 当请求头不匹配时，抛出此异常, 用于处理请求头不匹配的情况
 *
 * @version 1.0.0
 * @since 1.0.0
 * @see com.xlf.securivault.exceptions.PublicException
 * @author xiao_lfeng
 */
public class RequestHeaderNotMatchException extends RuntimeException {
    public RequestHeaderNotMatchException(String message) {
        super(message);
    }
}
