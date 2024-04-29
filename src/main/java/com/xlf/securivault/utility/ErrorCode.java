package com.xlf.securivault.utility;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 错误码
 * <hr/>
 * 用于定义系统中的错误码信息, 该类用于定义系统中的错误码信息，用于统一管理系统中的错误码信息
 *
 * @since v1.0.0
 * @version v1.0.0
 * @author xiao_lfeng
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    USER_NOT_LOGIN("UserNotLogin", 40101, "用户未登录"),
    USER_NOT_EXIST("UserNotExist", 40102, "用户不存在"),
    REQUEST_METHOD_NOT_ALLOWED("RequestMethodNotAllowed", 40301, "请求方法不允许"),
    REQUEST_BODY_PARAMETERS_ERROR("RequestBodyParametersError", 40001, "请求体参数错误"),
    PAGE_NOT_FOUND("PageNotFounded", 40401, "页面未找到"),
    SERVER_INTERNAL_ERROR("ServerInternalError", 50001, "服务器内部错误");

    private final String output;
    private final Integer code;
    private final String message;
}
