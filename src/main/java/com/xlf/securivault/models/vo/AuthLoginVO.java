package com.xlf.securivault.models.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * 用户登录对象
 * <hr/>
 * 用于定义用户登录对象，用于接收用户登录的请求参数；
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author xiao_lfeng
 */
@Getter
public class AuthLoginVO {
    /**
     * 用户名，支持邮箱、手机号、用户名进行登录
     */
    @NotBlank(message = "用户名不能为空")
    private String user;
    /**
     * 密码，用户登录的密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}
