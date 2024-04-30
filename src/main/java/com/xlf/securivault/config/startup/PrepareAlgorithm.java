/*
 * *******************************************************************************
 * Copyright (c) 2016-NOW(至今) 筱锋
 * Author: 筱锋(https://www.x-lf.com)
 *
 * 本文件包含 SecuriValue 的源代码，该项目的所有源代码均遵循MIT开源许可证协议。
 * 本代码仅进行 Java 大作业提交，个人发行版本计划使用 Go 语言重构。
 * *******************************************************************************
 * 许可证声明：
 *
 * 版权所有 (c) 2016-2024 筱锋。保留所有权利。
 *
 * 本软件是“按原样”提供的，没有任何形式的明示或暗示的保证，包括但不限于
 * 对适销性、特定用途的适用性和非侵权性的暗示保证。在任何情况下，
 * 作者或版权持有人均不承担因软件或软件的使用或其他交易而产生的、
 * 由此引起的或以任何方式与此软件有关的任何索赔、损害或其他责任。
 *
 * 由于作者需要进行 Java 大作业提交，所以请勿抄袭。您可以作为参考，但是
 * 一定不可以抄袭，尤其是同校同学！！！
 * 你们可以自己参考代码优化给你们提供思路，开源目的不是给你们抄袭的，共
 * 同维护好开源的社区环境！！！
 *
 * 使用本软件即表示您了解此声明并同意其条款。
 *
 * 有关MIT许可证的更多信息，请查看项目根目录下的LICENSE文件或访问：
 * https://opensource.org/licenses/MIT
 * *******************************************************************************
 * 免责声明：
 *
 * 使用本软件的风险由用户自担。作者或版权持有人在法律允许的最大范围内，
 * 对因使用本软件内容而导致的任何直接或间接的损失不承担任何责任。
 * *******************************************************************************
 */

package com.xlf.securivault.config.startup;

import com.xlf.securivault.utility.util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 准备算法
 * <hr/>
 * 用于定义系统启动时的准备算法，用于定义系统启动时的准备算法;
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author xiao_lfeng
 */
@Slf4j
@RequiredArgsConstructor
class PrepareAlgorithm {
    /**
     * JDBC 模板
     */
    private final JdbcTemplate jdbcTemplate;

    /**
     * 准备数据库
     * <hr/>
     * 用于准备数据库，用于准备数据库
     */
    public void table(String tableName) {
        // 检查数据库是否完整
        try {
            jdbcTemplate.queryForObject(
                    "SELECT table_name FROM information_schema.tables WHERE table_name = ?",
                    String.class,
                    "xf_" + tableName
            );
        } catch (DataAccessException e) {
            log.debug("[STARTUP] 创建数据表 {}", tableName);
            // 读取文件
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            // 读取 resources/sql 目录下的所有 SQL 文件
            Resource resource = resolver.getResource("classpath:/sql/" + tableName + ".sql");
            // 创建数据表
            try {
                String sql = FileCopyUtils
                        .copyToString(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
                // 分割 SQL 语句并执行
                String[] sqlStatements = sql.split(";");
                for (String statement : sqlStatements) {
                    if (!statement.trim().isEmpty()) {
                        jdbcTemplate.execute(statement.trim());
                    }
                }
            } catch (IOException ex) {
                log.error("[STARTUP] 创建数据表失败 | {}", ex.getMessage(), ex);
            }
        }
    }

    /**
     * 角色检查
     * <hr/>
     * 用于检查角色是否存在，用于检查角色是否存在;
     *
     * @param roleName 角色名
     */
    public void roleCheck(String roleName, String displayName, String description) {
        try {
            jdbcTemplate.queryForObject(
                    "SELECT name FROM public.xf_role WHERE name = ?",
                    String.class,
                    roleName
            );
        } catch (DataAccessException e) {
            log.debug("[STARTUP] 创建角色 [{}]{} - {}", roleName, displayName, description);
            jdbcTemplate.update(
                    "INSERT INTO public.xf_role (ruuid, name, display_name, description) VALUES (?,?,?,?)",
                    util.generateUuid(),
                    roleName,
                    displayName,
                    description
            );
        }
    }
}
