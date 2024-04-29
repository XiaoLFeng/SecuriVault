package com.xlf.securivault.config.configuration;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatisPlus配置类
 * <hr/>
 * 用于配置MyBatisPlus的一些配置, 例如分页插件等;
 *
 * @since v1.0.0
 * @version v1.0.0
 * @author xiao_lfeng
 */
@Slf4j
@Configuration
public class MybatisPlusConfig {

    /**
     * MyBatisPlus分页插件
     * <hr/>
     * 用于配置MyBatisPlus的分页插件, 用于分页查询; 该插件会自动拦截分页查询的请求, 并进行分页查询
     *
     * @return 分页
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        log.debug("[CONFIG] MyBatisPlus 分页开始配置");
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 分页
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setMaxLimit(20L);
        paginationInnerInterceptor.setDbType(DbType.POSTGRE_SQL);

        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        log.info("[CONFIG] MyBatisPlus 分页插件配置成功 | 分页插件配置成功");
        return interceptor;
    }
}
