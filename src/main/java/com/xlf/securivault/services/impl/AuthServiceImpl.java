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

package com.xlf.securivault.services.impl;

import com.xlf.securivault.dao.RoleDAO;
import com.xlf.securivault.dao.TokenDAO;
import com.xlf.securivault.dao.UserDAO;
import com.xlf.securivault.exceptions.BusinessException;
import com.xlf.securivault.models.dto.UserCurrentDTO;
import com.xlf.securivault.models.entity.RoleDO;
import com.xlf.securivault.models.entity.TokenDO;
import com.xlf.securivault.models.entity.UserDO;
import com.xlf.securivault.models.vo.AuthRegisterVO;
import com.xlf.securivault.services.AuthService;
import com.xlf.securivault.utility.ErrorCode;
import com.xlf.securivault.utility.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 * 认证服务
 * <hr/>
 * 用于定义认证服务，用于定义认证服务；用于处理用户的认证信息；
 *
 * @author xiao_lfeng
 * @version v1.0.0
 * @since v1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserDAO userDAO;
    private final TokenDAO tokenDAO;
    private final RoleDAO roleDAO;

    /**
     * 检查用户Token
     * <hr/>
     * 用于检查用户Token是否有效; 如果用户 Token 有效，返回 true；否则返回 false；
     *
     * @param userToken 用户Token
     * @return 是否有效
     */
    @Override
    public boolean checkUserToken(String userToken) {
        TokenDO getToken = tokenDAO.getTokenByToken(Util.tokenReplaceBearer(userToken));
        if (getToken != null) {
            return getToken.getExpiredAt().after(new Date());
        } else {
            return false;
        }
    }

    /**
     * 用户登录
     * <hr/>
     * 用于用户登录；用户支持用户名、邮箱、手机号登录；
     *
     * @param user     用户名
     * @param password 密码
     * @return 返回登录成功的用户信息
     */
    @Override
    public UserCurrentDTO userLogin(@NotNull String user, @NotNull String password) {
        // 处理用户登录
        UserDO getUser;
        if (user.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")) {
            getUser = userDAO.getUserByEmail(user);
        } else if (user.matches("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$")) {
            getUser = userDAO.getUserByPhone(user);
        } else {
            getUser = userDAO.getUserByUsername(user);
        }
        // 判断用户是否存在
        if (getUser != null) {
            // 判断用户是否有权限登录
            if (getUser.getBanned() || !getUser.getEnabled()) {
                throw new BusinessException("用户已被禁用或已被删除", ErrorCode.USER_BANNED);
            } else {
                // 判断用户密码是否正确
                if (Util.checkPassword(password, getUser.getPassword())) {
                    UserCurrentDTO userCurrentDTO = new UserCurrentDTO();
                    BeanUtils.copyProperties(getUser, userCurrentDTO);
                    return userCurrentDTO;
                } else {
                    throw new BusinessException("用户密码错误", ErrorCode.WRONG_PASSWORD);
                }
            }
        } else {
            return null;
        }
    }

    /**
     * 获取用户信息
     * <hr/>
     * 使用用户的 Token 获取用户信息；
     *
     * @param userToken 用户Token
     * @return 返回登录成功的用户信息
     */
    @Override
    public UserCurrentDTO userLoginWithToken(@NotNull String userToken) {
        if (!userToken.isBlank()) {
            TokenDO getToken = tokenDAO.getTokenByToken(Util.tokenReplaceBearer(userToken));
            if (getToken != null) {
                UserDO getUser = userDAO.getUserByUuid(getToken.getUuid());
                if (getUser != null) {
                    UserCurrentDTO userCurrentDTO = new UserCurrentDTO();
                    BeanUtils.copyProperties(getUser, userCurrentDTO);
                    return userCurrentDTO;
                }
            }
        }
        return null;
    }

    /**
     * 生成用户Token
     * <hr/>
     * 用于生成用户Token；
     *
     * @param getUser 用户信息
     * @return 返回生成的用户Token
     */
    @Override
    public UUID generateUserToken(@NotNull UserCurrentDTO getUser) {
        // 生成 Token
        UUID userToken = Util.generateUuid();
        TokenDO tokenDO = new TokenDO()
                .setToken(userToken.toString())
                .setUuid(getUser.getUuid())
                .setCreatedAt(new Timestamp(System.currentTimeMillis()))
                .setExpiredAt(new Timestamp(System.currentTimeMillis() + 1000 * 60 * 60 * 24));
        // 存入数据库
        tokenDAO.saveToken(tokenDO);
        return userToken;
    }

    /**
     * 用户注册
     * <hr/>
     * 用于用户注册；
     *
     * @param authRegisterVO 用户注册信息
     * @return 返回注册成功的用户信息
     */
    @Override
    public UserCurrentDTO userRegister(@NotNull AuthRegisterVO authRegisterVO) {
        // 判断用户是否存在
        if (userDAO.getUserByUsername(authRegisterVO.getUsername()) != null) {
            throw new BusinessException("用户 " + authRegisterVO.getUsername() + " 已存在", ErrorCode.USER_EXIST);
        }
        // 判断邮箱是否存在
        if (userDAO.getUserByEmail(authRegisterVO.getEmail()) != null) {
            throw new BusinessException("邮箱 " + authRegisterVO.getEmail() + " 已存在", ErrorCode.USER_EXIST);
        }
        // 判断手机号是否存在
        if (userDAO.getUserByPhone(authRegisterVO.getPhone()) != null) {
            throw new BusinessException("手机号 " + authRegisterVO.getPhone() + " 已存在", ErrorCode.USER_EXIST);
        }
        // 获取普通用户角色
        RoleDO getDefaultRole = roleDAO.getRoleByName("default");
        // 处理用户注册
        UserDO userDO = new UserDO()
                .setUuid(Util.generateUuid().toString())
                .setUsername(authRegisterVO.getUsername())
                .setPassword(Util.enPassword(authRegisterVO.getPassword()))
                .setEmail(authRegisterVO.getEmail())
                .setPhone(authRegisterVO.getPhone())
                .setRole(UUID.fromString(getDefaultRole.getRuuid()));
        // 存入数据库
        userDAO.saveUser(userDO);
        UserCurrentDTO userCurrentDTO = new UserCurrentDTO();
        BeanUtils.copyProperties(userDO, userCurrentDTO);
        return userCurrentDTO;
    }

    /**
     * 重置密码
     * <hr/>
     * 用于重置密码；
     *
     * @param email       邮箱
     * @param newPassword 新密码
     * @return 是否重置成功
     */
    @Override
    public boolean resetPassword(String email, String newPassword) {
        UserDO getUser = userDAO.getUserByEmail(email);
        if (getUser != null) {
            getUser.setPassword(Util.enPassword(newPassword));
            userDAO.updateById(getUser);
            return true;
        } else {
            return false;
        }
    }
}
