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

import com.xlf.securivault.constant.SystemConfigurationVariable;
import com.xlf.securivault.exceptions.BusinessException;
import com.xlf.securivault.models.vo.IndexInitialAdminVO;
import com.xlf.securivault.services.UserService;
import com.xlf.securivault.utility.BaseResponse;
import com.xlf.securivault.utility.ErrorCode;
import com.xlf.securivault.utility.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 中心控制器
 * <hr/>
 * 中心控制器，用于处理中心额外的请求；
 *
 * @author xiao_lfeng
 * @version v1.0.0
 * @since v1.0.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class IndexController {
    private final UserService userService;

    /**
     * 首页
     * <hr/>
     * 用于返回首页；
     *
     * @return 首页
     */
    @GetMapping("/")
    public ResponseEntity<BaseResponse<Void>> index() {
        return ResultUtil.success("Welcome to SecuriVault!");
    }

    /**
     * 检查是否需要初始化
     * <hr/>
     * 用于检查是否需要初始化；引导前端进入初始化页面；
     *
     * @return 初始化
     */
    @GetMapping("/initialize")
    public ResponseEntity<BaseResponse<Void>> needToInitialize() {
        if (SystemConfigurationVariable.getInitialize()) {
            return ResultUtil.success("系统已经初始化");
        } else {
            throw new BusinessException("系统需要初始化", ErrorCode.OPERATION_FAILED);
        }
    }

    /**
     * 初始化管理员
     * <hr/>
     * 用于初始化管理员；
     *
     * @param indexInitialAdminVO 初始化管理员视图对象
     * @return 初始化结果
     */
    @PostMapping("/initialize/admin")
    public ResponseEntity<BaseResponse<Void>> initializeAdmin(
            @RequestBody @Validated IndexInitialAdminVO indexInitialAdminVO
    ) {
        return userService.initialAdmin(indexInitialAdminVO);
    }
}
