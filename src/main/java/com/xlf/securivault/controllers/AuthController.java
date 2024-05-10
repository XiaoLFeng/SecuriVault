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

package com.xlf.securivault.controllers;

import com.xlf.securivault.exceptions.BusinessException;
import com.xlf.securivault.exceptions.library.UserAuthenticationException;
import com.xlf.securivault.models.dto.UserCurrentDTO;
import com.xlf.securivault.models.dto.UserLoginDTO;
import com.xlf.securivault.models.vo.AuthLoginVO;
import com.xlf.securivault.models.vo.AuthRegisterVO;
import com.xlf.securivault.services.AuthService;
import com.xlf.securivault.utility.BaseResponse;
import com.xlf.securivault.utility.ErrorCode;
import com.xlf.securivault.utility.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    /**
     * 用户登录
     * <hr/>
     * 用户登录，用于用户登录，支持用户名、邮箱、手机号登录；
     * 如果用户已经登录，将会返回用户的登录信息；
     *
     * @param authLoginVO 用户登录信息
     * @param userToken   用户 Token
     * @return 返回用户登录信息
     */
    @GetMapping("/login")
    public ResponseEntity<BaseResponse<UserLoginDTO>> authLogin(
            @RequestBody @Validated AuthLoginVO authLoginVO,
            @RequestHeader(value = "Authorization", required = false) String userToken
    ) {
        // 检查用户的授权认证是否有效
        if (userToken != null && !userToken.isEmpty()) {
            if (authService.checkUserToken(userToken)) {
                UserLoginDTO getUser = (UserLoginDTO) authService.userLoginWithToken(userToken);
                getUser.setToken(userToken);
                return ResultUtil.success("登录依然有效", getUser);
            } else {
                throw new UserAuthenticationException("授权 Token 无效或已过期");
            }
        }
        // 用户登录
        UserCurrentDTO getCurrentUser = authService.userLogin(authLoginVO.getUser(), authLoginVO.getPassword());
        if (getCurrentUser != null) {
            UserLoginDTO getUser = new UserLoginDTO();
            BeanUtils.copyProperties(getCurrentUser, getUser);
            // 生成 Token
            getUser.setToken(authService.generateUserToken(getUser).toString());
            return ResultUtil.success("登录成功", getUser);
        } else {
            throw new BusinessException("用户 " + authLoginVO.getUser() + " 不存在", ErrorCode.USER_NOT_EXIST);
        }
    }

    /**
     * 用户注册
     * <hr/>
     * 用户注册，用于用户注册；
     * 如果用户已经登录，将会返回用户的登录信息；
     *
     * @param authRegisterVO 用户注册信息
     * @param userToken      用户 Token
     * @return 返回用户登录信息
     */
    @PostMapping("/register")
    public ResponseEntity<BaseResponse<UserLoginDTO>> authRegister(
            @RequestBody @Validated AuthRegisterVO authRegisterVO,
            @RequestHeader(value = "Authorization", required = false) String userToken
    ) {
        // 检查用户的授权认证是否有效
        if (userToken != null && !userToken.isEmpty()) {
            if (authService.checkUserToken(userToken)) {
                UserLoginDTO getUser = (UserLoginDTO) authService.userLoginWithToken(userToken);
                getUser.setToken(userToken);
                return ResultUtil.success("认证依然有效", getUser);
            } else {
                throw new UserAuthenticationException("授权 Token 无效或已过期");
            }
        }
        // 用户注册
        UserCurrentDTO getCurrentUser = authService.userRegister(authRegisterVO);
        if (getCurrentUser != null) {
            UserLoginDTO getUser = new UserLoginDTO();
            BeanUtils.copyProperties(getCurrentUser, getUser);
            // 生成 Token
            getUser.setToken(authService.generateUserToken(getUser).toString());
            return ResultUtil.success("注册成功", getUser);
        } else {
            throw new BusinessException("用户创建失败", ErrorCode.USER_NOT_EXIST);
        }
    }
}
