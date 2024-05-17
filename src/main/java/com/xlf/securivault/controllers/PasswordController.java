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

package com.xlf.securivault.controllers;

import com.xlf.securivault.annotations.NeedAuthentication;
import com.xlf.securivault.annotations.NeedUserLogin;
import com.xlf.securivault.exceptions.BusinessException;
import com.xlf.securivault.models.dto.PasswordDTO;
import com.xlf.securivault.models.dto.PasswordSeeDTO;
import com.xlf.securivault.models.vo.PasswordAddVO;
import com.xlf.securivault.models.vo.PasswordEditVO;
import com.xlf.securivault.services.PasswordService;
import com.xlf.securivault.utility.BaseResponse;
import com.xlf.securivault.utility.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 密码控制器
 * <hr/>
 * 密码控制器，用于处理密码相关的请求；
 *
 * @since v1.0.0
 * @version v1.0.0
 * @author xiao_lfeng
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/password")
public class PasswordController {
    private final PasswordService passwordService;

    /**
     * 添加密码
     *
     * @param passwordAddVO 密码添加VO
     * @return 添加结果
     */
    @NeedUserLogin
    @PostMapping("/")
    public ResponseEntity<BaseResponse<Void>> addPassword(
            @RequestBody @Validated PasswordAddVO passwordAddVO,
            HttpServletRequest request
    ) {
        return passwordService.addPassword(passwordAddVO, request);
    }

    /**
     * 编辑密码
     *
     * @param passwordEditVO 密码编辑VO
     * @return 编辑结果
     */
    @NeedUserLogin
    @NeedAuthentication
    @PutMapping("/{passwordId}")
    public ResponseEntity<BaseResponse<Void>> editPassword(
            @PathVariable String passwordId,
            @RequestBody @Validated PasswordEditVO passwordEditVO,
            HttpServletRequest request
    ) {
        if (!passwordId.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")) {
            throw new BusinessException("密码ID格式错误", ErrorCode.REQUEST_PATH_ERROR);
        }
        return passwordService.editPassword(passwordEditVO, passwordId, request);
    }

    /**
     * 删除密码
     *
     * @param passwordId 密码ID
     * @return 删除结果
     */
    @NeedUserLogin
    @NeedAuthentication
    @DeleteMapping("/{passwordId}")
    public ResponseEntity<BaseResponse<Void>> deletePassword(
            @PathVariable String passwordId,
            HttpServletRequest request
    ) {
        if (!passwordId.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")) {
            throw new BusinessException("密码ID格式错误", ErrorCode.REQUEST_PATH_ERROR);
        }
        return passwordService.deletePassword(passwordId, request);
    }

    /**
     * 获取密码
     *
     * @param passwordId 密码ID
     * @return 获取结果
     */
    @NeedUserLogin
    @NeedAuthentication
    @GetMapping("/{passwordId}")
    public ResponseEntity<BaseResponse<PasswordSeeDTO>> getPassword(
            @PathVariable String passwordId,
            HttpServletRequest request
    ) {
        if (!passwordId.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")) {
            throw new BusinessException("密码ID格式错误", ErrorCode.REQUEST_PATH_ERROR);
        }
        return passwordService.getPassword(passwordId, request);
    }

    /**
     * 获取密码列表
     *
     * @return 获取结果
     */
    @NeedUserLogin
    @GetMapping("/list")
    public ResponseEntity<BaseResponse<PasswordDTO>> getPasswords(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            HttpServletRequest request
    ) {
        if (search != null && search.length() > 100) {
            throw new BusinessException("搜索内容过长", ErrorCode.REQUEST_PARAMETERS_ERROR);
        }
        if (page != null && !page.matches("^[1-9]\\d*$")) {
            throw new BusinessException("页码格式错误", ErrorCode.REQUEST_PARAMETERS_ERROR);
        }
        if (size != null && !size.matches("^[1-9]\\d*$")) {
            throw new BusinessException("每页数量格式错误", ErrorCode.REQUEST_PARAMETERS_ERROR);
        }
        return passwordService.getPasswords(search, page, size, request);
    }
}
