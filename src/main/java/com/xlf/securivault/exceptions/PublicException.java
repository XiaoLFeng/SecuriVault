package com.xlf.securivault.exceptions;

import com.xlf.securivault.exceptions.library.PageNotFoundedException;
import com.xlf.securivault.exceptions.library.RequestBodyParametersException;
import com.xlf.securivault.exceptions.library.SystemParameterError;
import com.xlf.securivault.utility.BaseResponse;
import com.xlf.securivault.utility.ErrorCode;
import com.xlf.securivault.utility.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * 业务异常处理
 * <hr/>
 * 用于处理业务异常, 该类中的方法用于处理自定义的业务异常，当业务异常发生时，将会自动捕获并处理，不会影响系统的正常运行
 *
 * @author xiao_lfeng
 * @version v1.0.0
 * @since v1.0.0
 */
@Slf4j
public class PublicException {

    /**
     * 异常处理
     * <hr/>
     * 用于处理业务异常, 当业务异常发生时，将会自动捕获并处理，不会影响系统的正常运行
     *
     * @param e 异常信息 Exception
     * @return 返回异常信息
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Exception>> handleException(Exception e) {
        log.error("[EXCEPTION] 未定义输出异常 | {}", e.getMessage(), e);
        // 异常处理
        return ResultUtil.error(ErrorCode.SERVER_INTERNAL_ERROR, "未定义输出异常", e);
    }

    /**
     * 页面未找到异常处理
     * <hr/>
     * 用于处理页面未找到异常, 当页面未找到异常发生时，将会自动捕获并处理，不会影响系统的正常运行
     *
     * @param e 页面未找到异常 PageNotFoundedException
     * @return 返回异常信息
     */
    @ExceptionHandler(PageNotFoundedException.class)
    public ResponseEntity<BaseResponse<PageNotFoundedException>> handlePageNotFoundException(
            PageNotFoundedException e
    ) {
        log.warn("[EXCEPTION] 页面未找到异常 | {}<{}>", e.getMessage(), e.getRoute());
        // 异常处理
        return ResultUtil.error(ErrorCode.PAGE_NOT_FOUND, "页面未找到", e);
    }

    /**
     * 业务异常处理
     * <hr/>
     * 用于处理业务异常, 当业务异常发生时，将会自动捕获并处理，不会影响系统的正常运行;
     * 该方法用于处理自定义的业务异常, 主要处理由于业务的主动错误返回产生的异常
     *
     * @param e 业务异常 BusinessException
     * @return 返回异常信息
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BaseResponse<Object>> handleBusinessException(BusinessException e) {
        log.warn("[EXCEPTION] 业务异常 | {}<{}>", e.getMessage(), e.getErrorCode());
        // 异常处理
        return ResultUtil.error(e.getErrorCode(), e.getErrorMessage(), e.getData());
    }

    /**
     * 空指针异常处理
     * <hr/>
     * 用于处理空指针异常, 当空指针异常发生时，将会自动捕获并处理，不会影响系统的正常运行
     *
     * @param e 空指针异常 NullPointerException
     * @return 返回异常信息
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<BaseResponse<NullPointerException>> handleNullPointerException(NullPointerException e) {
        log.error("[EXCEPTION] 空指针异常 | {}", e.getMessage(), e);
        // 异常处理
        return ResultUtil.error(ErrorCode.SERVER_INTERNAL_ERROR, "空指针异常", e);
    }

    /**
     * 系统参数错误处理
     * <hr/>
     * 用于处理系统参数错误, 当系统参数错误发生时，将会自动捕获并处理，不会影响系统的正常运行
     *
     * @param e 系统参数错误 SystemParameterError
     * @return 返回异常信息
     */
    @ExceptionHandler(SystemParameterError.class)
    public ResponseEntity<BaseResponse<SystemParameterError>> handleSystemParameterError(SystemParameterError e) {
        log.error("[EXCEPTION] 系统参数错误 | {}", e.getMessage(), e);
        // 异常处理
        return ResultUtil.error(ErrorCode.SERVER_INTERNAL_ERROR, "系统参数错误", e);
    }

    /**
     * 请求方法不支持异常处理
     * <hr/>
     * 用于处理请求方法不支持异常, 当请求方法不支持异常发生时，将会自动捕获并处理，不会影响系统的正常运行
     *
     * @param e 请求方法不支持异常 HttpRequestMethodNotSupportedException
     * @return 返回异常信息
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<BaseResponse<HashMap<String, Object>>> handleHttpRequestMethodNotSupportedException(
            @NotNull HttpRequestMethodNotSupportedException e
    ) {
        log.warn("[EXCEPTION] 请求方法不支持 | 获取的方法 [{}] ,需要的方法 {}", e.getMethod(), e.getSupportedHttpMethods());
        // 异常处理
        HashMap<String, Object> data = new HashMap<>();
        data.put("method", "[" + e.getMethod() + "]");
        data.put("supported", Objects.requireNonNull(e.getSupportedHttpMethods()).toString());
        return ResultUtil.error(ErrorCode.REQUEST_METHOD_NOT_ALLOWED, "请求方法不支持", data);
    }

    /**
     * 请求头缺失异常处理
     * <hr/>
     * 用于处理请求头缺失异常, 当请求头缺失异常发生时，将会自动捕获并处理，不会影响系统的正常运行
     *
     * @param e 请求头缺失异常 MissingRequestHeaderException
     * @return 返回异常信息
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<BaseResponse<Void>> handleMissingRequestHeaderException(
            @NotNull MissingRequestHeaderException e
    ) {
        log.warn("[EXCEPTION] 请求头缺失 | 缺失的请求头 [{}]", e.getHeaderName());
        // 异常处理
        return switch (e.getHeaderName()) {
            case "Authorization", "X-User-UUID" -> ResultUtil.error(
                    ErrorCode.USER_NOT_LOGIN,
                    "用户未登录",
                    null
            );
            default -> ResultUtil.error(
                    ErrorCode.REQUEST_METHOD_NOT_ALLOWED,
                    "请求头 " + e.getHeaderName() + " 缺失",
                    null
            );
        };
    }

    @ExceptionHandler(RequestBodyParametersException.class)
    public ResponseEntity<BaseResponse<List<RequestBodyParametersException.ErrorFunc>>>
    handleRequestBodyParametersException(@NotNull RequestBodyParametersException e) {
        log.warn(
                "[EXCEPTION] 请求体参数错误 | {} (...{}个错误)",
                e.getErrorInformation().get(0).getMessage(),
                e.getErrorCount() - 1
        );
        return ResultUtil.error(
                ErrorCode.REQUEST_BODY_PARAMETERS_ERROR,
                e.getErrorInformation().get(0).getMessage(),
                e.getErrorInformation()
        );
    }

}
