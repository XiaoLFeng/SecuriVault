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

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 标准输出统一接口
 * <hr/>
 * 用于输出一个标准化的接口信息, 用于输出一个标准化的接口信息，该接口信息包含了返回的状态码、返回的信息、返回的数据
 *
 * @author xiao_lfeng
 * @version v1.0.0
 * @since v1.0.0
 */
@Slf4j
@Component
public class ResultUtil {

    /**
     * 成功输出
     * <hr/>
     * 输出一个成功的结果, 用于输出一个成功的结果, 用于输出一个成功的结果
     * 但是该输出结果不包含数据
     *
     * @param message 输出的 message 信息
     * @return 输出一个成功的结果
     */
    public static @NotNull ResponseEntity<BaseResponse<Void>> success(String message) {
        log.info("[RESULT] [{}]{} | {}", 200, "Success", "成功");
        return ResponseEntity
                .ok(new BaseResponse<>("Success", 200, message, null, null));
    }

    /**
     * 成功输出
     * <hr/>
     * 输出一个成功的结果, 用于输出一个成功的结果, 用于输出一个成功的结果
     * 该输出结果包含数据
     *
     * @param message 输出的 message 信息
     * @param data    输出的数据
     * @return 输出一个成功的结果
     */
    public static <T> @NotNull ResponseEntity<BaseResponse<T>> success(String message, @NotNull T data) {
        log.info("[RESULT] [{}]{} | {}(数据: {})", 200, "Success", "成功", data.getClass().getSimpleName());
        return ResponseEntity
                .ok(new BaseResponse<>("Success", 200, message, null, data));
    }

    /**
     * 失败输出
     * <hr/>
     * 输出一个失败的结果, 用于输出一个失败的结果, 用于输出一个失败的结果, 输出的结果包含数据，需要传入错误码(ErrorCode)和错误信息；
     * 请注意，失败的输出不应该在此直接调用，应当在业务中出现错误直接进行抛出异常，然后在全局异常处理中进行处理；
     * 不应当在代码中直接处理，否则会导致代码的可读性和可维护性降低；以及无法处理数据库事务的问题；
     *
     * @param errorCode    错误码 - 用于定义系统中的错误码信息，用于统一管理系统中的错误码信息
     * @param errorMessage 错误信息 - 用于定义系统中的错误信息，用于统一管理系统中的错误信息
     * @param data         输出的数据
     * @return 输出一个失败的结果
     */
    public static <T> @NotNull ResponseEntity<BaseResponse<T>> error(
            ErrorCode errorCode,
            String errorMessage,
            @Nullable T data
    ) {
        if (data != null) {
            log.info(
                    "[RESULT] [{}]{} | {}:{}(数据: {})",
                    errorCode.getCode(),
                    errorCode.getOutput(),
                    errorCode.getMessage(),
                    errorMessage,
                    data.getClass().getSimpleName()
            );
        } else {
            log.info(
                    "[RESULT] [{}]{} | {}:{}",
                    errorCode.getCode(),
                    errorCode.getOutput(),
                    errorCode.getMessage(),
                    errorMessage
            );
        }
        return ResponseEntity
                .status(errorCode.getCode() / 100)
                .body(new BaseResponse<>(
                        errorCode.getOutput(),
                        errorCode.getCode(),
                        errorCode.getMessage(),
                        errorMessage,
                        data
                ));
    }
}
