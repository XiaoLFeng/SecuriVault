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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xlf.securivault.dao.LogsDAO;
import com.xlf.securivault.dao.TokenLibraryDAO;
import com.xlf.securivault.dao.UserDAO;
import com.xlf.securivault.exceptions.BusinessException;
import com.xlf.securivault.models.dto.TokenDTO;
import com.xlf.securivault.models.dto.TokenGeneralDTO;
import com.xlf.securivault.models.dto.TokenSeeDTO;
import com.xlf.securivault.models.entity.TokenLibraryDO;
import com.xlf.securivault.models.entity.UserDO;
import com.xlf.securivault.models.vo.TokenAddVO;
import com.xlf.securivault.services.TokenService;
import com.xlf.securivault.utility.BaseResponse;
import com.xlf.securivault.utility.ErrorCode;
import com.xlf.securivault.utility.ResultUtil;
import com.xlf.securivault.utility.Util;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * 令牌服务
 * <hr/>
 * 令牌服务，用于定义令牌服务；
 *
 * @version v1.0.0
 * @since v1.0.0
 * @author xiao_lfeng
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final UserDAO userDAO;
    private final TokenLibraryDAO tokenLibraryDAO;
    private final LogsDAO logsDAO;

    /**
     * 添加令牌
     *
     * @param tokenAddVO 令牌添加VO
     * @param request    请求
     * @return 添加令牌结果
     */
    @Override
    public ResponseEntity<BaseResponse<Void>> addToken(TokenAddVO tokenAddVO, HttpServletRequest request) {
        String getUserUuid = Util.getUserUuid(request);
        if (getUserUuid == null) {
            throw new AuthorizationServiceException("用户未登录");
        }
        UserDO getUser = userDAO.getUserByUuid(getUserUuid);
        if (getUser == null) {
            throw new AuthorizationServiceException("用户不存在");
        }
        // 检查令牌信息是否已存在
        TokenLibraryDO checkTokenExist = tokenLibraryDAO.checkTokenExist(tokenAddVO, getUserUuid);
        if (checkTokenExist != null) {
            if (!tokenAddVO.isForce()) {
                throw new BusinessException("令牌信息已存在", ErrorCode.OPERATION_FAILED);
            }
        }
        // 添加令牌
        TokenLibraryDO newTokenDO = new TokenLibraryDO();
        BeanUtils.copyProperties(tokenAddVO, newTokenDO);
        newTokenDO
                .setUuid(getUserUuid)
                .setAccessKey(Util.passwordLibraryEncode(tokenAddVO.getAccessKey()))
                .setSecretKey(Util.passwordLibraryEncode(tokenAddVO.getSecretKey()));
        // 保存令牌(更新或添加)
        if (checkTokenExist != null) {
            newTokenDO.setId(checkTokenExist.getId());
        } else {
            newTokenDO.setId(Util.generateUuid().toString());
        }
        if (checkTokenExist == null) {
            if (tokenLibraryDAO.save(newTokenDO)) {
                logsDAO.addTokenLog(getUserUuid, newTokenDO, "令牌", "添加令牌");
                return ResultUtil.success("添加令牌成功");
            } else {
                throw new BusinessException("添加令牌失败", ErrorCode.OPERATION_FAILED);
            }
        } else {
            if (tokenLibraryDAO.updateById(newTokenDO)) {
                logsDAO.addTokenLog(getUserUuid, newTokenDO, "令牌", "重添令牌");
                return ResultUtil.success("添加令牌成功");
            } else {
                throw new BusinessException("添加令牌失败", ErrorCode.OPERATION_FAILED);
            }
        }
    }

    /**
     * 删除令牌
     *
     * @param tokenId 令牌ID
     * @param request 请求
     * @return 删除令牌结果
     */
    @Override
    public ResponseEntity<BaseResponse<Void>> deleteToken(String tokenId, HttpServletRequest request) {
        String getUserUuid = Util.getUserUuid(request);
        if (getUserUuid == null) {
            throw new AuthorizationServiceException("用户未登录");
        }
        UserDO getUser = userDAO.getUserByUuid(getUserUuid);
        if (getUser == null) {
            throw new AuthorizationServiceException("用户不存在");
        }
        TokenLibraryDO getToken = tokenLibraryDAO.getTokenById(tokenId, getUserUuid);
        if (getToken == null) {
            throw new BusinessException("令牌不存在", ErrorCode.OPERATION_FAILED);
        }
        if (tokenLibraryDAO.remove(new QueryWrapper<TokenLibraryDO>()
                .eq("id", tokenId)
                .eq("uuid", getUserUuid))
        ) {
            logsDAO.addTokenLog(getUserUuid, getToken, "令牌", "删除令牌");
            return ResultUtil.success("删除令牌成功");
        } else {
            throw new BusinessException("删除令牌失败", ErrorCode.OPERATION_FAILED);
        }
    }

    /**
     * 获取令牌
     *
     * @param tokenId 令牌ID
     * @param request 请求
     * @return 令牌
     */
    @Override
    public ResponseEntity<BaseResponse<TokenSeeDTO>> getToken(String tokenId, HttpServletRequest request) {
        String getUserUuid = Util.getUserUuid(request);
        if (getUserUuid == null) {
            throw new AuthorizationServiceException("用户未登录");
        }
        UserDO getUser = userDAO.getUserByUuid(getUserUuid);
        if (getUser == null) {
            throw new AuthorizationServiceException("用户不存在");
        }
        TokenLibraryDO getToken = tokenLibraryDAO.getTokenById(tokenId, getUserUuid);
        if (getToken == null) {
            throw new BusinessException("令牌不存在", ErrorCode.OPERATION_FAILED);
        }
        TokenSeeDTO seeToken = new TokenSeeDTO();
        BeanUtils.copyProperties(getToken, seeToken);
        seeToken.setAccessKey(Util.passwordLibraryDecode(getToken.getAccessKey()));
        seeToken.setSecretKey(Util.passwordLibraryDecode(getToken.getSecretKey()));
        logsDAO.addTokenLog(getUserUuid, getToken, "令牌", "查看令牌");
        return ResultUtil.success("获取令牌成功", seeToken);
    }

    /**
     * 获取令牌
     *
     * @param search  搜索
     * @param page    页码
     * @param limit   大小
     * @param request 请求
     * @return 令牌
     */
    @Override
    public ResponseEntity<BaseResponse<TokenDTO>> getTokens(
            String search,
            Long page,
            Long limit,
            HttpServletRequest request
    ) {
        String getUserUuid = Util.getUserUuid(request);
        if (getUserUuid == null) {
            throw new AuthorizationServiceException("用户未登录");
        }
        UserDO getUser = userDAO.getUserByUuid(getUserUuid);
        if (getUser == null) {
            throw new AuthorizationServiceException("用户不存在");
        }
        Page<TokenLibraryDO> userAllToken = tokenLibraryDAO.getUserAllToken(getUserUuid, search, page, limit);
        TokenDTO tokenDTO = new TokenDTO();
        ArrayList<TokenDTO.Token> tokenList = new ArrayList<>();
        tokenDTO.setTotal(userAllToken.getTotal());
        tokenDTO.setPage(userAllToken.getCurrent());
        tokenDTO.setSize(userAllToken.getSize());
        userAllToken.getRecords().forEach(tokenLibraryDO -> {
            TokenDTO.Token token = new TokenDTO.Token();
            BeanUtils.copyProperties(tokenLibraryDO, token);
            token.setAccessKey(Util.maskKey(Util.passwordLibraryDecode(tokenLibraryDO.getAccessKey())));
            tokenList.add(token);
        });
        tokenDTO.setToken(tokenList);
        return ResultUtil.success("获取令牌成功", tokenDTO);
    }

    /**
     * 获取通用令牌
     *
     * @param request 请求
     * @return 通用令牌
     */
    @Override
    public ResponseEntity<BaseResponse<TokenGeneralDTO>> getTokenGeneral(HttpServletRequest request) {
        String getUserUuid = Util.getUserUuid(request);
        if (getUserUuid == null) {
            throw new AuthorizationServiceException("用户未登录");
        }
        UserDO getUser = userDAO.getUserByUuid(getUserUuid);
        if (getUser == null) {
            throw new AuthorizationServiceException("用户不存在");
        }
        Long totalToken = tokenLibraryDAO.getUserAllTokenNoDelete(getUserUuid);
        if (totalToken != null) {
            Long removeToken = tokenLibraryDAO.getUserRecentlyRemoveToken(getUserUuid);
            TokenGeneralDTO generalToken = new TokenGeneralDTO();
            generalToken.setTotalToken(totalToken - removeToken);
            generalToken.setRecentlyAdd(tokenLibraryDAO.getUserRecentlyAddToken(getUserUuid));
            generalToken.setRecentlyGet(tokenLibraryDAO.getUserRecentlySeeToken(getUserUuid));
            generalToken.setRecentlyRemove(removeToken);
            return ResultUtil.success("获取通用令牌成功", generalToken);
        } else {
            TokenGeneralDTO generalToken = new TokenGeneralDTO();
            generalToken.setTotalToken(0L);
            generalToken.setRecentlyAdd(0L);
            generalToken.setRecentlyGet(0L);
            generalToken.setRecentlyRemove(0L);
            return ResultUtil.success("获取通用令牌成功", generalToken);
        }
    }
}
