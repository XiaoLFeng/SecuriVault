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

package com.xlf.securivault.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xlf.securivault.mappers.TokenLibraryMapper;
import com.xlf.securivault.models.entity.TokenLibraryDO;
import com.xlf.securivault.models.vo.TokenAddVO;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Token库DAO
 * <hr/>
 * Token库DAO，用于定义Token库DAO；
 *
 * @version v1.0.0
 * @since v1.0.0
 * @author xiao_lfeng
 */
@Repository
public class TokenLibraryDAO
        extends ServiceImpl<TokenLibraryMapper, TokenLibraryDO>
        implements IService<TokenLibraryDO> {

    /**
     * 检查Token是否存在
     *
     * @param tokenAddVO 令牌添加VO
     * @param getUserUuid 获取用户UUID
     * @return Token库DO
     */
    public TokenLibraryDO checkTokenExist(@NotNull TokenAddVO tokenAddVO, String getUserUuid) {
        return this.lambdaQuery()
                .eq(TokenLibraryDO::getUuid, getUserUuid)
                .eq(TokenLibraryDO::getAccessKey, tokenAddVO.getAccessKey())
                .eq(TokenLibraryDO::getSecretKey, tokenAddVO.getSecretKey())
                .one();
    }

    /**
     * 通过Token ID获取Token
     *
     * @param tokenId 令牌ID
     * @param getUserUuid 获取用户UUID
     * @return Token库DO
     */
    public TokenLibraryDO getTokenById(String tokenId, String getUserUuid) {
        return this.lambdaQuery()
                .eq(TokenLibraryDO::getUuid, getUserUuid)
                .eq(TokenLibraryDO::getId, tokenId)
                .one();
    }

    /**
     * 获取用户所有Token
     *
     * @param getUserUuid 获取用户UUID
     * @param search 搜索
     * @param page 页码
     * @param limit 限制
     */
    public Page<TokenLibraryDO> getUserAllToken(String getUserUuid, String search, Long page, Long limit) {
        return this.lambdaQuery()
                .eq(TokenLibraryDO::getUuid, getUserUuid)
                .like(TokenLibraryDO::getAccessKey, search)
                .isNull(TokenLibraryDO::getDeletedAt)
                .or()
                .eq(TokenLibraryDO::getUuid, getUserUuid)
                .isNull(TokenLibraryDO::getDeletedAt)
                .page(new Page<>(page, limit));
    }

    /**
     * 获取用户所有Token
     *
     * @param getUserUuid 获取用户UUID
     * @return Token库DO
     */
    public Long getUserAllTokenNoDelete(String getUserUuid) {
        return this.lambdaQuery()
                .eq(TokenLibraryDO::getUuid, getUserUuid)
                .count();
    }

    /**
     * 获取用户最近添加的Token
     *
     * @param getUserUuid 获取用户UUID
     * @return Token库DO
     */
    public Long getUserRecentlyAddToken(String getUserUuid) {
        Date lastSevenDays = new Date(System.currentTimeMillis() - 604800000);
        return this.lambdaQuery()
                .eq(TokenLibraryDO::getUuid, getUserUuid)
                .gt(TokenLibraryDO::getCreatedAt, lastSevenDays)
                .isNull(TokenLibraryDO::getDeletedAt)
                .count();
    }

    /**
     * 获取用户最近查看的Token
     *
     * @param getUserUuid 获取用户UUID
     * @return Token库DO
     */
    public Long getUserRecentlySeeToken(String getUserUuid) {
        Date lastSevenDays = new Date(System.currentTimeMillis() - 604800000);
        return this.lambdaQuery()
                .eq(TokenLibraryDO::getUuid, getUserUuid)
                .gt(TokenLibraryDO::getSeeTime, lastSevenDays)
                .isNull(TokenLibraryDO::getDeletedAt)
                .count();
    }

    /**
     * 获取用户最近删除的Token
     *
     * @param getUserUuid 获取用户UUID
     * @return Token库DO
     */
    public Long getUserRecentlyRemoveToken(String getUserUuid) {
        Date lastSevenDays = new Date(System.currentTimeMillis() - 604800000);
        return this.lambdaQuery()
                .eq(TokenLibraryDO::getUuid, getUserUuid)
                .gt(TokenLibraryDO::getDeletedAt, lastSevenDays)
                .count();
    }
}
