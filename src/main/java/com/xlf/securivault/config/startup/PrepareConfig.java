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

import com.xlf.securivault.constant.SystemConfigurationVariable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 准备配置
 * <hr/>
 * 用于定义系统启动时的准备配置，用于定义系统启动时的准备配置;
 *
 * @author xiao_lfeng
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class PrepareConfig {
    private final Environment environment;

    /**
     * JDBC 模板
     */
    private final JdbcTemplate jdbcTemplate;
    /**
     * 准备算法
     */
    private PrepareAlgorithm prepare;

    /**
     * 系统启动准备
     * <hr/>
     * 用于定义系统启动时的准备配置；
     *
     * @return CommandLineRunner
     */
    @Bean
    @Order(1)
    public CommandLineRunner prepareStart() {
        return args -> {
            log.info("========== SYSTEM PREPARE START ==========");
            prepare = new PrepareAlgorithm(jdbcTemplate);
        };
    }

    /**
     * 系统启动准备
     * <hr/>
     * 用于处理服务缺失对应数据表处理的资源；
     *
     * @return CommandLineRunner
     */
    @Bean
    @Order(2)
    public CommandLineRunner prepareOne() {
        return args -> {
            log.info("[STARTUP] 开始检查数据库是否完整");
            // 请不要修改数据表初始化顺序，涉及到数据表之间的关联关系
            prepare.table("info");
            prepare.table("phone_verify_code");
            prepare.table("email_verify_code");
            prepare.table("role");
            prepare.table("user");
            prepare.table("password_library");
        };
    }

    /**
     * 系统启动准备
     * <hr/>
     * 用于处理服务缺失对应数据表处理的资源；
     * 用于检查角色组是否完整
     *
     * @return CommandLineRunner
     */
    @Bean
    @Order(3)
    public CommandLineRunner prepareTwo() {
        return args -> {
            log.info("[STARTUP] 检查角色组是否完整");
            prepare.roleCheck("admin", "管理员", "系统使用的管理员，具有最高权限");
            prepare.roleCheck("default", "普通用户", "系统默认的普通用户，具有基本权限");
        };
    }

    /**
     * 系统启动准备
     * <hr/>
     * 用于用于处理服务准备的环境变量；
     *
     * @return CommandLineRunner
     */
    @Bean
    @Order(99)
    public CommandLineRunner prepareEnv() {
        return args -> {
            // 获取配置文件中的版本号
            SystemConfigurationVariable.VERSION = environment.getProperty("version");
        };
    }

    /**
     * 系统启动准备
     * <hr/>
     * 用于定义系统启动完毕后需要释放的资源；
     *
     * @return CommandLineRunner
     */
    @Bean
    @Order(100)
    public CommandLineRunner prepareFinal() {
        return args -> {
            log.info("[STARTUP] 初始化检查完毕");
            log.info("========== SYSTEM PREPARE FINAL ==========");
            System.out.print("""
                    \033[35;1m
                          ____                 _ _   __          ____\s
                         / __/__ ______ ______(_) | / /__ ___ __/ / /_
                        _\\ \\/ -_) __/ // / __/ /| |/ / _ `/ // / / __/
                       /___/\\__/\\__/\\_,_/_/ /_/ |___/\\_,_/\\_,_/_/\\__/\s
                    """);
            System.out.print("   \033[32;1m::: SecuriVault :::             ");
            System.out.printf("\033[36;1m::: %s :::\033[0m\n\n", SystemConfigurationVariable.VERSION);
        };
    }
}
