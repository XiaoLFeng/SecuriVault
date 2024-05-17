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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xlf.securivault.dao.PasswordLibraryDAO;
import com.xlf.securivault.exceptions.BusinessException;
import com.xlf.securivault.models.entity.PasswordLibraryDO;
import com.xlf.securivault.models.vo.PasswordAddVO;
import com.xlf.securivault.services.PermissionService;
import com.xlf.securivault.utility.BaseResponse;
import com.xlf.securivault.utility.ErrorCode;
import com.xlf.securivault.utility.ResultUtil;
import com.xlf.securivault.utility.Util;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * 权限服务实现
 * <hr/>
 * 权限服务实现，用于实现权限服务；
 *
 * @version v1.0.0
 * @since v1.0.0
 * @author xiao_lfeng
 */
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PasswordLibraryDAO passwordLibraryDAO;

    /**
     * 添加权限
     *
     * @param passwordAddVO 密码添加VO
     * @return 添加结果
     */
    @Override
    public ResponseEntity<BaseResponse<Void>> addPermission(
            @NotNull PasswordAddVO passwordAddVO,
            HttpServletRequest request
    ) {
        String getUserUuid = Util.getUserUuid(request);
        PasswordLibraryDO getPassword = passwordLibraryDAO
                .checkPasswordExist(passwordAddVO.getWebsite(), passwordAddVO.getUsername(), getUserUuid);
        if (getPassword != null) {
            if (!passwordAddVO.getForce()) {
                throw new BusinessException("密码已存在，若强制添加", ErrorCode.OPERATION_FAILED);
            }
        }
        // 添加密码
        if (getPassword != null) {
            getPassword.setWebsite(passwordAddVO.getWebsite());
            getPassword.setUsername(passwordAddVO.getUsername());
            getPassword.setPassword(Util.passwordLibraryEncode(passwordAddVO.getPassword()));
            getPassword.setOther(passwordAddVO.getOther());
            getPassword.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            passwordLibraryDAO.update(
                    getPassword,
                    new QueryWrapper<PasswordLibraryDO>()
                            .eq("id", getPassword.getId())
            );
            if (passwordLibraryDAO.save(getPassword)) {
                return ResultUtil.success("添加密码成功");
            } else {
                throw new BusinessException("添加密码失败", ErrorCode.OPERATION_FAILED);
            }
        } else {
            PasswordLibraryDO setPassword = new PasswordLibraryDO();
            setPassword.setId(Util.generateUuid().toString());
            setPassword.setUuid(getUserUuid);
            setPassword.setWebsite(passwordAddVO.getWebsite());
            setPassword.setUsername(passwordAddVO.getUsername());
            setPassword.setPassword(Util.passwordLibraryEncode(passwordAddVO.getPassword()));
            setPassword.setOther(passwordAddVO.getOther());
            if (passwordLibraryDAO.save(setPassword)) {
                return ResultUtil.success("添加密码成功");
            } else {
                throw new BusinessException("添加密码失败", ErrorCode.OPERATION_FAILED);
            }
        }
    }
}
