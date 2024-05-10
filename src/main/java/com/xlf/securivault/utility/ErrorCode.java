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
    WRONG_PASSWORD("WrongPassword", 40103, "密码错误"),
    USER_BANNED("UserBanned", 40104, "用户被封禁或未启用"),
    REQUEST_METHOD_NOT_ALLOWED("RequestMethodNotAllowed", 40301, "请求方法不允许"),
    REQUEST_BODY_PARAMETERS_ERROR("RequestBodyParametersError", 40001, "请求体参数错误"),
    REQUEST_PARAMETERS_ERROR("RequestParametersError", 40002, "请求参数错误"),
    REQUEST_PATH_ERROR("RequestPathError", 40003, "请求路径参数错误"),
    USER_EXIST("UserExist", 40004, "用户已存在"),
    PAGE_NOT_FOUND("PageNotFounded", 40401, "页面未找到"),
    SERVER_INTERNAL_ERROR("ServerInternalError", 50001, "服务器内部错误");

    private final String output;
    private final Integer code;
    private final String message;
}
