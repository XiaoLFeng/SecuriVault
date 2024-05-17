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
import com.xlf.securivault.mappers.PasswordLibraryMapper;
import com.xlf.securivault.models.entity.PasswordLibraryDO;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 密码库DAO
 * <hr/>
 * 密码库DAO，用于定义密码库DAO；
 *
 * @version v1.0.0
 * @since v1.0.0
 * @author xiao_lfeng
 */
@Repository
public class PasswordLibraryDAO
        extends ServiceImpl<PasswordLibraryMapper, PasswordLibraryDO>
        implements IService<PasswordLibraryDO> {
    /**
     * 检查密码是否存在
     * <hr/>
     * 检查密码是否存在；根据网站页面以及用户名检查密码是否存在；
     *
     * @param website  网站
     * @param username 用户名
     * @return 密码库DO
     */
    public PasswordLibraryDO checkPasswordExist(String website, String username, String user) {
        return this.lambdaQuery()
                .eq(PasswordLibraryDO::getWebsite, website)
                .eq(PasswordLibraryDO::getUsername, username)
                .eq(PasswordLibraryDO::getUuid, user)
                .one();
    }

    /**
     * 根据ID获取密码
     * <hr/>
     * 根据ID获取密码；根据ID获取密码；
     *
     * @param id         ID
     * @param getUserUuid 用户UUID
     * @return 密码库DO
     */
    public PasswordLibraryDO getPasswordById(String id, String getUserUuid) {
        return this.lambdaQuery()
                .eq(PasswordLibraryDO::getId, id)
                .eq(PasswordLibraryDO::getUuid, getUserUuid)
                .one();
    }

    /**
     * 获取所有密码
     * <hr/>
     * 获取所有密码；获取所有密码；
     *
     * @param getUserUuid 用户UUID
     * @param search      搜索
     * @param page        页码
     * @param size        大小
     * @return 密码库DO列表
     */
    public Page<PasswordLibraryDO> getUserAllPassword(String getUserUuid, String search, String page, String size) {
        if (page == null || page.isEmpty()) {
            page = "1";
        }
        if (size == null || size.isEmpty()) {
            size = "20";
        }
        Page<PasswordLibraryDO> rowPage = new Page<>(Long.parseLong(page), Long.parseLong(size));
        if (search == null || search.isEmpty()) {
            return this.lambdaQuery()
                    .eq(PasswordLibraryDO::getUuid, getUserUuid)
                    .page(rowPage);
        } else {
            return this.lambdaQuery()
                    .eq(PasswordLibraryDO::getUuid, getUserUuid)
                    .like(PasswordLibraryDO::getWebsite, search)
                    .page(rowPage);
        }
    }

    /**
     * 获取密码总数
     * <hr/>
     * 获取密码总数；获取密码总数；
     *
     * @param getUserUuid 用户UUID
     * @return 密码总数
     */
    public Long getUserPasswordTotal(String getUserUuid) {
        return this.lambdaQuery()
                .eq(PasswordLibraryDO::getUuid, getUserUuid)
                .isNull(PasswordLibraryDO::getDeletedAt)
                .count();
    }

    /**
     * 获取最近添加的密码
     * <hr/>
     * 获取最近添加的密码；获取最近添加的密码；
     *
     * @param getUserUuid 用户UUID
     * @return 最近添加的密码
     */
    public Long getUserPasswordRecentlyAdd(String getUserUuid) {
        Date lastSevenDays = new Date(System.currentTimeMillis() - 604800000);
        return this.lambdaQuery()
                .eq(PasswordLibraryDO::getUuid, getUserUuid)
                .isNull(PasswordLibraryDO::getDeletedAt)
                .gt(PasswordLibraryDO::getCreatedAt, new Timestamp(lastSevenDays.getTime()))
                .count();
    }

    /**
     * 获取最近查看的密码
     * <hr/>
     * 获取最近查看的密码；获取最近查看的密码；
     *
     * @param getUserUuid 用户UUID
     * @return 最近查看的密码
     */
    public Long getUserPasswordRecentlyGet(String getUserUuid) {
        Date lastSevenDays = new Date(System.currentTimeMillis() - 604800000);
        return this.lambdaQuery()
                .eq(PasswordLibraryDO::getUuid, getUserUuid)
                .isNull(PasswordLibraryDO::getDeletedAt)
                .gt(PasswordLibraryDO::getSeeTime, new Timestamp(lastSevenDays.getTime()))
                .count();
    }

    /**
     * 获取最近删除的密码
     * <hr/>
     * 获取最近删除的密码；获取最近删除的密码；
     *
     * @param getUserUuid 用户UUID
     * @return 最近删除的密码
     */
    public Long getUserPasswordRecentlyRemove(String getUserUuid) {
        Date lastSevenDays = new Date(System.currentTimeMillis() - 604800000);
        return this.lambdaQuery()
                .eq(PasswordLibraryDO::getUuid, getUserUuid)
                .isNotNull(PasswordLibraryDO::getDeletedAt)
                .gt(PasswordLibraryDO::getDeletedAt, new Timestamp(lastSevenDays.getTime()))
                .count();
    }
}
