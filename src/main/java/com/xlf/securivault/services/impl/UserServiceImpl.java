/*
 * ******************************************************************************
 *  Copyright (c) 2016-NOW(至今) 筱锋
 *  Author: 筱锋(https://www.x-lf.com)
 *
 *  本文件包含 SecuriValue 的源代码，该项目的所有源代码均遵循MIT开源许可证协议。
 *  本代码仅进行 Java 大作业提交，个人发行版本计划使用 Go 语言重构。
 * ******************************************************************************
 *  许可证声明：
 *
 *  版权所有 (c) 2016-2024 筱锋。保留所有权利。
 *
 *  本软件是“按原样”提供的，没有任何形式的明示或暗示的保证，包括但不限于
 *  对适销性、特定用途的适用性和非侵权性的暗示保证。在任何情况下，
 *  作者或版权持有人均不承担因软件或软件的使用或其他交易而产生的、
 *  由此引起的或以任何方式与此软件有关的任何索赔、损害或其他责任。
 *
 *  由于作者需要进行 Java 大作业提交，所以请勿抄袭。您可以作为参考，但是
 *  一定不可以抄袭，尤其是同校同学！！！
 *  你们可以自己参考代码优化给你们提供思路，开源目的不是给你们抄袭的，共
 *  同维护好开源的社区环境！！！
 *
 *  使用本软件即表示您了解此声明并同意其条款。
 *
 *  有关MIT许可证的更多信息，请查看项目根目录下的LICENSE文件或访问：
 *  https://opensource.org/licenses/MIT
 * ******************************************************************************
 *  免责声明：
 *
 *  使用本软件的风险由用户自担。作者或版权持有人在法律允许的最大范围内，
 *  对因使用本软件内容而导致的任何直接或间接的损失不承担任何责任。
 * ******************************************************************************
 */

package com.xlf.securivault.services.impl;

import com.xlf.securivault.constant.SystemConfigurationVariable;
import com.xlf.securivault.dao.InfoDAO;
import com.xlf.securivault.dao.UserDAO;
import com.xlf.securivault.exceptions.BusinessException;
import com.xlf.securivault.exceptions.library.UserAuthenticationException;
import com.xlf.securivault.models.dto.UserCurrentDTO;
import com.xlf.securivault.models.entity.InfoDO;
import com.xlf.securivault.models.entity.UserDO;
import com.xlf.securivault.models.vo.IndexInitialAdminVO;
import com.xlf.securivault.services.MailService;
import com.xlf.securivault.services.UserService;
import com.xlf.securivault.utility.BaseResponse;
import com.xlf.securivault.utility.ErrorCode;
import com.xlf.securivault.utility.ResultUtil;
import com.xlf.securivault.utility.Util;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * 用户服务实现
 * <hr/>
 * 用户服务实现，用于实现用户服务；
 *
 * @author xiao_lfeng
 * @version v1.0.0
 * @since v1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;
    private final InfoDAO infoDAO;
    private final MailService mailService;

    /**
     * 获取当前用户
     *
     * @param userUuid  用户UUID
     * @return 用户信息
     */
    @Override
    public ResponseEntity<BaseResponse<UserCurrentDTO>> getUserCurrent(String userUuid) {
        // 检查登录是否有效
        UserDO getUser = userDAO.getUserByUuid(userUuid);
        if (getUser != null) {
            UserCurrentDTO userCurrentDTO = new UserCurrentDTO();
            BeanUtils.copyProperties(getUser, userCurrentDTO);
            return ResultUtil.success("获取当前用户信息成功", userCurrentDTO);
        } else {
            throw new UserAuthenticationException("获取当前用户信息失败");
        }
    }

    /**
     * 初始化管理员
     *
     * @param indexInitialAdminVO 初始化管理员视图对象
     * @return 初始化结果
     */
    @Override
    public ResponseEntity<BaseResponse<Void>> initialAdmin(IndexInitialAdminVO indexInitialAdminVO) {
        if (SystemConfigurationVariable.getInitialize()) {
            throw new BusinessException("系统已经初始化", ErrorCode.OPERATION_FAILED);
        }
        // 初始化管理员
        UserDO userDO = userDAO.getUserByUsername("admin");
        if (userDO == null) {
            throw new BusinessException("初始化管理员失败", ErrorCode.OPERATION_FAILED);
        }
        userDO.setUsername(indexInitialAdminVO.getUsername())
                .setPassword(Util.enPassword(indexInitialAdminVO.getPassword()))
                .setEmail(indexInitialAdminVO.getEmail())
                .setPhone(indexInitialAdminVO.getPhone())
                .setVerifyEmail(true)
                .setVerifyPhone(true);
        if (userDAO.updateById(userDO)) {
            InfoDO getInitialize = infoDAO.lambdaQuery().eq(InfoDO::getKey, "initialize").one()
                    .setValue("1")
                    .setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            infoDAO.updateById(getInitialize);
            SystemConfigurationVariable.setInitialize(true);
            return ResultUtil.success("初始化管理员成功");
        } else {
            throw new BusinessException("初始化管理员失败", ErrorCode.OPERATION_FAILED);
        }
    }

    @Override
    public ResponseEntity<BaseResponse<Void>> certificationRequired(String userUuid) {
        UserDO getUser = userDAO.getUserByUuid(userUuid);
        if (getUser != null) {
            if (getUser.getVerifyTime() == null) {
                throw new BusinessException("请重新认证", ErrorCode.NEED_RE_AUTHENTICATION);
            } else {
                if (getUser.getVerifyTime().before(new Timestamp(System.currentTimeMillis() - 900000))) {
                    throw new BusinessException("请重新认证", ErrorCode.NEED_RE_AUTHENTICATION);
                }
                return ResultUtil.success("认证有效");
            }
        } else {
            throw new BusinessException("获取当前用户信息失败", ErrorCode.OPERATION_FAILED);
        }
    }

    /**
     * 发送授权
     *
     * @return 发送授权结果
     */
    @Override
    public ResponseEntity<BaseResponse<Void>> sendAuthorization(HttpServletRequest request) {
        String getUserUuid = Util.getUserUuid(request);
        UserDO getUser = userDAO.getUserByUuid(getUserUuid);
        if (getUser == null) {
            throw new BusinessException("获取当前用户信息失败", ErrorCode.OPERATION_FAILED);
        }
        mailService.sendUserAuthorizationMail(getUser, request);
        return ResultUtil.success("发送授权成功");
    }
}
