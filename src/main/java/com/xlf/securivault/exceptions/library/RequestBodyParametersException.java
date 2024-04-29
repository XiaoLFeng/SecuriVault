package com.xlf.securivault.exceptions.library;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 请求体参数异常
 * <hr/>
 * 用于请求体参数异常的异常类，用于请求体参数异常时抛出异常
 *
 * @since v1.0.0
 * @version v1.0.0
 * @see com.xlf.securivault.exceptions.PublicException
 * @author xiao_lfeng
 */
@Getter
public class RequestBodyParametersException extends RuntimeException {
    /**
     * 所有错误信息
     */
    private final List<ObjectError> allErrors;
    /**
     * 错误数量
     */
    private final Integer errorCount;
    /**
     * 错误信息
     */
    private final List<ErrorFunc> errorInformation = new ArrayList<>();

    /**
     * 构造方法
     *
     * @param message 异常信息
     * @param bindingResult 绑定结果
     */
    public RequestBodyParametersException(String message, BindingResult bindingResult) {
        super(message);
        this.allErrors = bindingResult.getAllErrors();
        this.errorCount = bindingResult.getErrorCount();
        bindingResult.getFieldErrors().forEach(it -> this.errorInformation.add(new ErrorFunc(
                it.getObjectName(),
                it.getDefaultMessage(),
                it.getRejectedValue() == null ? null : Objects.requireNonNull(it.getRejectedValue()).toString(),
                it.getCode()
        )));
    }

    /**
     * 错误信息
     * <hr/>
     * 用于返回错误信息的内部类, 用于返回错误信息的内部类, 包括字段名、错误信息、错误值、错误码
     */
    @Getter
    @AllArgsConstructor
    public static class ErrorFunc {
        private String field;
        private String message;
        private String value;
        private String code;
    }
}
