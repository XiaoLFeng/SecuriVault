package com.xlf.securivault.exceptions;

import com.xlf.securivault.utility.ErrorCode;
import lombok.Getter;

/**
 * 自定义异常
 * <hr/>
 * 当自定义异常发生时，将会自动捕获并处理，不会影响系统的正常运行；
 * 这个自定义异常类主要用于返回给前端的自定义业务抛出信息，使用在信息返回部分
 *
 * @author xiao_lfeng
 * @version v1.0.0
 * @see RuntimeException
 * @see PublicException
 * @since v1.0.0
 */
@Getter
public class BusinessException extends RuntimeException {
    /**
     * 错误码
     */
    private final ErrorCode errorCode;
    /**
     * 错误信息
     */
    private final String errorMessage;
    /**
     * 附加数据
     */
    private final Object data;


    /**
     * 构造函数
     * <hr/>
     * 构造一个自定义异常, 用于处理自定义的业务异常, 需要指定异常信息，异常信息将会被打印到日志中以及返回前端部分在 errorMessage 中;
     * 附加数据将会被返回给前端部分在 data 中
     *
     * @param message   异常信息
     * @param errorCode 错误码
     * @param data      附加数据
     */
    public BusinessException(String message, ErrorCode errorCode, Object data) {
        super(message);
        this.errorCode = errorCode;
        this.errorMessage = message;
        this.data = data;
    }

    /**
     * 构造函数
     * <hr/>
     * 构造一个自定义异常, 用于处理自定义的业务异常, 需要指定异常信息，异常信息将会被打印到日志中以及返回前端部分在 errorMessage 中;
     * 没有额外的附加参数
     *
     * @param message   异常信息
     * @param errorCode 错误码
     */
    public BusinessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.errorMessage = message;
        this.data = null;
    }
}
