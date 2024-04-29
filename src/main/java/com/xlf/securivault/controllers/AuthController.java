package com.xlf.securivault.controllers;

import com.xlf.securivault.models.dto.UserCurrentDTO;
import com.xlf.securivault.models.vo.AuthLoginVO;
import com.xlf.securivault.utility.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录控制器
 * <hr/>
 * 登录控制器，用于处理登录相关的请求；
 *
 * @author xiao_lfeng
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@RestController("/api/v1/auth")
public class AuthController {

    @GetMapping("/login")
    public ResponseEntity<BaseResponse<UserCurrentDTO>> authLogin(
            @RequestBody @Validated AuthLoginVO authLoginVO,
            @RequestHeader(value = "Authorization", required = false) String userToken
    ) {
        return null;
    }

}
