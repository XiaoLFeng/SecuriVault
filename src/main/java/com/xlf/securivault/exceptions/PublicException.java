/*
 * *******************************************************************************
 * Copyright (c) 2016-NOW(至今) 筱锋
 * Author: 筱锋(https://www.x-lf.com)
 *
 * 本文件包含 SecuriValue 的源代码，该项目的所有源代码均遵循MIT开源许可证协议。
 * 本代码仅进行 Java 大作业提交，个人发行版本计划使用 Go 语言重构。
 * *******************************************************************************
 * 许可证声明：
 *
 * 版权所有 (c) 2016-2024 筱锋。保留所有权利。
 *
 * 本软件是“按原样”提供的，没有任何形式的明示或暗示的保证，包括但不限于
 * 对适销性、特定用途的适用性和非侵权性的暗示保证。在任何情况下，
 * 作者或版权持有人均不承担因软件或软件的使用或其他交易而产生的、
 * 由此引起的或以任何方式与此软件有关的任何索赔、损害或其他责任。
 *
 * 由于作者需要进行 Java 大作业提交，所以请勿抄袭。您可以作为参考，但是
 * 一定不可以抄袭，尤其是同校同学！！！
 * 你们可以自己参考代码优化给你们提供思路，开源目的不是给你们抄袭的，共
 * 同维护好开源的社区环境！！！
 *
 * 使用本软件即表示您了解此声明并同意其条款。
 *
 * 有关MIT许可证的更多信息，请查看项目根目录下的LICENSE文件或访问：
 * https://opensource.org/licenses/MIT
 * *******************************************************************************
 * 免责声明：
 *
 * 使用本软件的风险由用户自担。作者或版权持有人在法律允许的最大范围内，
 * 对因使用本软件内容而导致的任何直接或间接的损失不承担任何责任。
 * *******************************************************************************
 */

package com.xlf.securivault.exceptions;

import com.xlf.securivault.exceptions.library.*;
import com.xlf.securivault.utility.BaseResponse;
import com.xlf.securivault.utility.ErrorCode;
import com.xlf.securivault.utility.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
@RestControllerAdvice
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
    public ResponseEntity<BaseResponse<Object>> handleBusinessException(@NotNull BusinessException e) {
        log.warn("[EXCEPTION] <{}>{} | {}", e.getErrorCode().getCode(), e.getErrorCode().getMessage(), e.getMessage());
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

    /**
     * 请求体参数异常处理
     * <hr/>
     * 用于处理请求体参数异常, 当请求体参数异常发生时，将会自动捕获并处理，不会影响系统的正常运行
     *
     * @param e 请求体参数异常 RequestBodyParametersException
     * @return 返回异常信息
     */
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

    /**
     * 请求头不匹配异常处理
     * <hr/>
     * 用于处理请求头不匹配异常, 当请求头不匹配异常发生时，将会自动捕获并处理，不会影响系统的正常运行
     *
     * @param e 请求头不匹配异常 RequestHeaderNotMatchException
     * @return 返回异常信息
     */
    @ExceptionHandler(RequestHeaderNotMatchException.class)
    public ResponseEntity<BaseResponse<RequestHeaderNotMatchException>> handleRequestHeaderNotMatchException(
            @NotNull RequestHeaderNotMatchException e
    ) {
        log.warn("[EXCEPTION] 请求头不匹配异常 | {}", e.getMessage());
        return ResultUtil.error(ErrorCode.REQUEST_METHOD_NOT_ALLOWED, e.getMessage(), null);
    }

    /**
     * 参数校验错误处理
     * <hr/>
     * 用于处理参数校验错误, 当参数校验错误发生时，将会自动捕获并处理，不会影响系统的正常运行
     *
     * @param e 参数校验错误 MethodArgumentNotValidException
     * @return 返回异常信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<List<String>>> handleMethodArgumentNotValidException(
            @NotNull MethodArgumentNotValidException e
    ) {
        log.warn("[EXCEPTION] 参数校验错误 | 错误 {} 个 ", e.getBindingResult().getErrorCount());
        e.getFieldErrors().stream().toList().forEach(
                it -> log.debug("\t\t<{}>[{}]: {}", it.getField(), it.getRejectedValue(), it.getDefaultMessage())
        );
        return ResultUtil.error(
                ErrorCode.REQUEST_BODY_PARAMETERS_ERROR,
                e.getAllErrors().stream().map(ObjectError::getDefaultMessage).toList().get(0),
                e.getAllErrors().stream().map(ObjectError::getDefaultMessage).toList()
        );
    }

    /**
     * 邮件模板不存在异常处理
     * <hr/>
     * 用于处理邮件模板不存在异常, 当邮件模板不存在异常发生时，将会自动捕获并处理，不会影响系统的正常运行
     *
     * @param e 邮件模板不存在异常 MailTemplateNotFoundException
     * @return 返回异常信息
     */
    @ExceptionHandler(MailTemplateNotFoundException.class)
    public ResponseEntity<BaseResponse<MailTemplateNotFoundException>> handleMailTemplateNotFoundException(
            @NotNull MailTemplateNotFoundException e
    ) {
        log.warn("[EXCEPTION] 邮件模板不存在 | {}", e.getMessage());
        return ResultUtil.error(ErrorCode.MAIL_ERROR, "邮件模板 " + e.getMessage() + " 不存在", null);
    }

    /**
     * 邮件发送异常处理
     * <hr/>
     * 用于处理邮件发送异常, 当邮件发送异常发生时，将会自动捕获并处理，不会影响系统的正常运行
     *
     * @param e 邮件发送异常 MailSendException
     * @return 返回异常信息
     */
    @ExceptionHandler(MailSendException.class)
    public ResponseEntity<BaseResponse<MailSendException>> handleMailSendException(
            @NotNull MailSendException e
    ) {
        log.error("[EXCEPTION] 邮件发送异常 | {}", e.getMessage(), e);
        return ResultUtil.error(ErrorCode.MAIL_ERROR, "邮件发送异常", e);
    }

    /**
     * 用户认证异常处理
     * <hr/>
     * 用于处理用户认证异常, 当用户认证异常发生时，将会自动捕获并处理，不会影响系统的正常运行
     *
     * @param e 用户认证异常 UserAuthenticationException
     * @return 返回异常信息
     */
    @ExceptionHandler(UserAuthenticationException.class)
    public ResponseEntity<BaseResponse<UserAuthenticationException>> handleUserAuthenticationException(
            @NotNull UserAuthenticationException e
    ) {
        log.error("[EXCEPTION] 用户认证异常 | {}", e.getMessage(), e);
        return ResultUtil.error(ErrorCode.USER_NOT_LOGIN, "Token 授权不存在或已过期", e);
    }
}
