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
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.xlf.securivault.dao.LogsDAO;
import com.xlf.securivault.dao.PasswordLibraryDAO;
import com.xlf.securivault.exceptions.BusinessException;
import com.xlf.securivault.models.dto.PasswordDTO;
import com.xlf.securivault.models.dto.PasswordGeneralDTO;
import com.xlf.securivault.models.dto.PasswordSeeDTO;
import com.xlf.securivault.models.entity.PasswordLibraryDO;
import com.xlf.securivault.models.vo.PasswordAddVO;
import com.xlf.securivault.models.vo.PasswordEditVO;
import com.xlf.securivault.services.PasswordService;
import com.xlf.securivault.utility.BaseResponse;
import com.xlf.securivault.utility.ErrorCode;
import com.xlf.securivault.utility.ResultUtil;
import com.xlf.securivault.utility.Util;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
public class PasswordServiceImpl implements PasswordService {

    private final PasswordLibraryDAO passwordLibraryDAO;
    private final Gson gson;
    private final LogsDAO logsDAO;

    /**
     * 添加权限
     *
     * @param passwordAddVO 密码添加VO
     * @return 添加结果
     */
    @Override
    public ResponseEntity<BaseResponse<Void>> addPassword(
            @NotNull PasswordAddVO passwordAddVO,
            HttpServletRequest request
    ) {
        String getUserUuid = Util.getUserUuid(request);
        PasswordLibraryDO getPassword = passwordLibraryDAO
                .checkPasswordExist(passwordAddVO.getWebsite(), passwordAddVO.getUsername(), getUserUuid);
        if (getPassword != null) {
            if (!passwordAddVO.isForce()) {
                throw new BusinessException("密码已存在，请直接修改信息", ErrorCode.OPERATION_FAILED);
            }
        }
        // 添加密码
        if (getPassword != null) {
            getPassword.setWebsite(passwordAddVO.getWebsite());
            getPassword.setUsername(passwordAddVO.getUsername());
            getPassword.setPassword(Util.passwordLibraryEncode(passwordAddVO.getPassword()));
            getPassword.setOther(passwordAddVO.getOther());
            getPassword.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            if (getPassword.getDeletedAt() == null) {
                boolean status = passwordLibraryDAO.update(
                        getPassword,
                        new QueryWrapper<PasswordLibraryDO>()
                                .eq("id", getPassword.getId())
                );
                if (status) {
                    logsDAO.addPasswordLog(getUserUuid, getPassword, " Web", "重新添加密码");
                    return ResultUtil.success("添加密码成功");
                } else {
                    throw new BusinessException("添加密码失败", ErrorCode.OPERATION_FAILED);
                }
            } else {
                getPassword.setDeletedAt(null);
                boolean status = passwordLibraryDAO.update(
                        getPassword,
                        new QueryWrapper<PasswordLibraryDO>()
                                .eq("id", getPassword.getId())
                );
                if (status) {
                    logsDAO.addPasswordLog(getUserUuid, getPassword, " Web", "强制添加密码");
                    return ResultUtil.success("密码强制修改成功");
                } else {
                    throw new BusinessException("添加密码失败", ErrorCode.OPERATION_FAILED);
                }
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
                logsDAO.addPasswordLog(getUserUuid, setPassword, " Web", "添加密码");
                return ResultUtil.success("添加密码成功");
            } else {
                throw new BusinessException("添加密码失败", ErrorCode.OPERATION_FAILED);
            }
        }
    }

    /**
     * 编辑密码
     *
     * @param passwordEditVO 密码编辑VO
     * @param request 请求
     * @return 编辑结果
     */
    @Override
    public ResponseEntity<BaseResponse<Void>> editPassword(
            @NotNull PasswordEditVO passwordEditVO,
            String passwordId,
            HttpServletRequest request
    ) {
        String getUserUuid = Util.getUserUuid(request);
        // 根据 id 获取密码
        PasswordLibraryDO getPassword = passwordLibraryDAO.getPasswordById(passwordId, getUserUuid);
        if (getPassword == null) {
            throw new BusinessException("密码不存在", ErrorCode.OPERATION_FAILED);
        }
        // 编辑密码
        getPassword.setPassword(Util.passwordLibraryEncode(passwordEditVO.getPassword()));
        if (passwordEditVO.getOther() != null) {
            getPassword.setOther(passwordEditVO.getOther());
        }
        getPassword.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        boolean status = passwordLibraryDAO.update(
                getPassword,
                new QueryWrapper<PasswordLibraryDO>()
                        .eq("id", getPassword.getId())
        );
        if (status) {
            logsDAO.addPasswordLog(getUserUuid, getPassword, " Web", "编辑密码");
            return ResultUtil.success("编辑密码成功");
        } else {
            throw new BusinessException("编辑密码失败", ErrorCode.OPERATION_FAILED);
        }
    }

    /**
     * 删除密码
     *
     * @param passwordId 密码ID
     * @param request    请求
     * @return 删除结果
     */
    @Override
    public ResponseEntity<BaseResponse<Void>> deletePassword(String passwordId, HttpServletRequest request) {
        String getUserUuid = Util.getUserUuid(request);
        // 根据 id 获取密码
        PasswordLibraryDO getPassword = passwordLibraryDAO.getPasswordById(passwordId, getUserUuid);
        if (getPassword == null) {
            throw new BusinessException("密码不存在", ErrorCode.OPERATION_FAILED);
        }
        // 删除密码
        boolean status = passwordLibraryDAO.update(new UpdateWrapper<PasswordLibraryDO>()
                .eq("id", getPassword.getId())
                .set("deleted_at", new Timestamp(System.currentTimeMillis()))
        );
        if (status) {
            logsDAO.addPasswordLog(getUserUuid, getPassword, " Web", "删除密码");
            return ResultUtil.success("删除密码成功");
        } else {
            throw new BusinessException("删除密码失败", ErrorCode.OPERATION_FAILED);
        }
    }

    /**
     * 获取密码
     *
     * @param passwordId 密码ID
     * @param request    请求
     * @return 获取结果
     */
    @Override
    public ResponseEntity<BaseResponse<PasswordSeeDTO>> getPassword(String passwordId, HttpServletRequest request) {
        String getUserUuid = Util.getUserUuid(request);
        // 根据 id 获取密码
        PasswordLibraryDO getPassword = passwordLibraryDAO.getPasswordById(passwordId, getUserUuid);
        if (getPassword == null) {
            throw new BusinessException("密码不存在", ErrorCode.OPERATION_FAILED);
        }
        getPassword.setPassword(Util.passwordLibraryDecode(getPassword.getPassword()));
        PasswordSeeDTO seePassword = new PasswordSeeDTO();
        BeanUtils.copyProperties(getPassword, seePassword);
        if (seePassword.getOther() != null) {
            seePassword.setOther(gson.fromJson(getPassword.getOther(), HashMap.class));
        }
        logsDAO.addPasswordLog(getUserUuid, getPassword, " Web", "查看密码");
        return ResultUtil.success("密码获取成功", seePassword);
    }

    /**
     * 获取密码列表
     *
     * @param search  搜索
     * @param page    页码
     * @param size    大小
     * @param request 请求
     * @return 获取结果
     */
    @Override
    public ResponseEntity<BaseResponse<PasswordDTO>> getPasswords(
            String search,
            String page, String size,
            HttpServletRequest request
    ) {
        String getUserUuid = Util.getUserUuid(request);
        // 获取密码列表
        Page<PasswordLibraryDO> getPasswords = passwordLibraryDAO.getUserAllPassword(getUserUuid, search, page, size);
        if (!getPasswords.getRecords().isEmpty()) {
            List<PasswordLibraryDO> getPasswordList = getPasswords.getRecords();
            PasswordDTO passwords = new PasswordDTO();
            List<PasswordDTO.Password> passwordList = new ArrayList<>();
            for (PasswordLibraryDO getPassword : getPasswordList) {
                PasswordDTO.Password password = new PasswordDTO.Password();
                BeanUtils.copyProperties(getPassword, password);
                password.setUsername(Util.maskKey(getPassword.getUsername()));
                passwordList.add(password);
            }
            passwords.setPage(getPasswords.getCurrent());
            passwords.setSize(getPasswords.getSize());
            passwords.setPassword(passwordList);
            return ResultUtil.success("密码列表获取成功", passwords);
        } else {
            PasswordDTO passwords = new PasswordDTO();
            passwords.setPage(0L);
            passwords.setSize(0L);
            passwords.setPassword(new ArrayList<>());
            return ResultUtil.success("没有密码", passwords);
        }
    }

    /**
     * 获取密码的通用信息
     *
     * @param request 请求
     * @return 获取结果
     */
    @Override
    public ResponseEntity<BaseResponse<PasswordGeneralDTO>> getPasswordGeneral(HttpServletRequest request) {
        String getUserUuid = Util.getUserUuid(request);
        Long getCount = passwordLibraryDAO.getUserPasswordTotal(getUserUuid);
        Long removeCount = passwordLibraryDAO.getUserPasswordRecentlyRemove(getUserUuid);
        // 获取密码的通用信息
        if (getCount > 0) {
            PasswordGeneralDTO generalPassword = new PasswordGeneralDTO();
            generalPassword.setTotalPassword(getCount - removeCount);
            generalPassword.setRecentlyAdd(passwordLibraryDAO.getUserPasswordRecentlyAdd(getUserUuid));
            generalPassword.setRecentlyGet(passwordLibraryDAO.getUserPasswordRecentlyGet(getUserUuid));
            generalPassword.setRecentlyRemove(removeCount);
            return ResultUtil.success("密码通用信息获取成功", generalPassword);
        } else {
            PasswordGeneralDTO generalPassword = new PasswordGeneralDTO();
            generalPassword.setTotalPassword(0L);
            generalPassword.setRecentlyAdd(0L);
            generalPassword.setRecentlyGet(0L);
            generalPassword.setRecentlyRemove(0L);
            return ResultUtil.success("没有密码", generalPassword);
        }
    }
}
