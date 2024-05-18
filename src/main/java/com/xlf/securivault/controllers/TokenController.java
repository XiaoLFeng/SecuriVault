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
import com.xlf.securivault.models.dto.TokenDTO;
import com.xlf.securivault.models.dto.TokenGeneralDTO;
import com.xlf.securivault.models.dto.TokenSeeDTO;
import com.xlf.securivault.models.vo.TokenAddVO;
import com.xlf.securivault.services.TokenService;
import com.xlf.securivault.utility.BaseResponse;
import com.xlf.securivault.utility.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 令牌控制器
 * <hr/>
 * 日志控制器，用于处理日志相关的请求；
 *
 * @author xiao_lfeng
 * @version v1.0.0
 * @since v1.0.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/token")
public class TokenController {
    private final TokenService tokenService;

    /**
     * 添加Token
     *
     * @return ResponseEntity<BaseResponse < Void>>
     */
    @NeedUserLogin
    @NeedAuthentication
    @PostMapping("")
    public ResponseEntity<BaseResponse<Void>> addToken(
            @RequestBody @Validated TokenAddVO tokenAddVO,
            HttpServletRequest request
    ) {
        return tokenService.addToken(tokenAddVO, request);
    }

    /**
     * 删除Token
     *
     * @return ResponseEntity<BaseResponse < Void>>
     */
    @NeedUserLogin
    @NeedAuthentication
    @DeleteMapping("/{tokenId}")
    public ResponseEntity<BaseResponse<Void>> deleteToken(@PathVariable String tokenId, HttpServletRequest request) {
        if (!tokenId.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")) {
            throw new BusinessException("令牌 ID 错误", ErrorCode.REQUEST_PATH_ERROR);
        }
        return tokenService.deleteToken(tokenId, request);
    }

    /**
     * 获取Token
     *
     * @return ResponseEntity<BaseResponse < Void>>
     */
    @NeedUserLogin
    @GetMapping("/{tokenId}")
    public ResponseEntity<BaseResponse<TokenSeeDTO>> getToken(
            @PathVariable String tokenId,
            HttpServletRequest request
    ) {
        if (!tokenId.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")) {
            throw new BusinessException("令牌 ID 错误", ErrorCode.REQUEST_PATH_ERROR);
        }
        return tokenService.getToken(tokenId, request);
    }

    /**
     * 获取Token列表
     *
     * @return ResponseEntity<BaseResponse < Void>>
     */
    @NeedUserLogin
    @GetMapping("/list")
    public ResponseEntity<BaseResponse<TokenDTO>> getTokens(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "1", required = false) String page,
            @RequestParam(value = "limit", defaultValue = "20", required = false) String limit,
            HttpServletRequest request
    ) {
        if (!page.matches("[^0-9]+$")) {
            throw new BusinessException("页码或大小格式错误", ErrorCode.REQUEST_PATH_ERROR);
        }
        if (!limit.matches("[^0-9]+$")) {
            throw new BusinessException("页码或大小格式错误", ErrorCode.REQUEST_PATH_ERROR);
        }
        return tokenService.getTokens(search, Long.parseLong(page), Long.parseLong(limit), request);
    }

    /**
     * 获取Token概要
     *
     * @return ResponseEntity<BaseResponse < Void>>
     */
    @NeedUserLogin
    @GetMapping("/general")
    public ResponseEntity<BaseResponse<TokenGeneralDTO>> getTokenGeneral(
            HttpServletRequest request
    ) {
        return tokenService.getTokenGeneral(request);
    }
}
