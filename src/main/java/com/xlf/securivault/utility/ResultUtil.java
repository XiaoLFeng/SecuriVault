package com.xlf.securivault.utility;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
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
    public static ResponseEntity<BaseResponse<Void>> success(String message) {
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
    public static <T> ResponseEntity<BaseResponse<T>> success(String message, T data) {
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
    public static <T> ResponseEntity<BaseResponse<T>> error(
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
