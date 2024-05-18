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
import com.xlf.securivault.mappers.LogsMapper;
import com.xlf.securivault.models.entity.LogsDO;
import com.xlf.securivault.models.entity.PasswordLibraryDO;
import com.xlf.securivault.models.entity.TokenLibraryDO;
import com.xlf.securivault.utility.Util;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

/**
 * 日志DAO
 * <hr/>
 * 日志DAO，用于定义日志DAO；
 *
 * @author xiao_lfeng
 * @version v1.0.0
 * @since v1.0.0
 */
@Repository
public class LogsDAO extends ServiceImpl<LogsMapper, LogsDO> implements IService<LogsDO> {
    /**
     * 添加日志
     * <hr/>
     * 添加日志；用于添加日志；
     *
     * @param userUuid  用户 UUID
     * @param password  密码
     * @param type      类型
     * @param operation 操作
     */
    @Async
    public void addPasswordLog(
            String userUuid,
            @NotNull PasswordLibraryDO password,
            String type,
            String operation
    ) {
        LogsDO log = new LogsDO();
        log.setType(type)
                .setControls(operation)
                .setSite(password.getWebsite())
                .setUsername(Util.maskKey(password.getUsername()))
                .setUuid(userUuid);
        this.save(log);
    }

    @Async
    public void addTokenLog(
            String userUuid,
            @NotNull TokenLibraryDO token,
            String type,
            String operation
    ) {
        LogsDO log = new LogsDO();
        log.setType(type)
                .setSite(token.getSite())
                .setControls(operation)
                .setUuid(userUuid);
        if (token.getAccessKey() != null) {
            log.setUsername(Util.maskKey(Util.passwordLibraryDecode(token.getAccessKey())));
        }
        this.save(log);
    }

    /**
     * 获取日志
     * <hr/>
     * 获取日志；用于获取日志；
     *
     * @param userUuid 用户 UUID
     * @param page     页数
     * @param size     大小
     * @return 日志
     */
    public Page<LogsDO> getLogsByUserUuid(String userUuid, Long page, Long size) {
        Page<LogsDO> pages = new Page<>(page, size);
        return this.lambdaQuery()
                .eq(LogsDO::getUuid, userUuid)
                .orderByDesc(LogsDO::getControlsAt)
                .page(pages);
    }
}
