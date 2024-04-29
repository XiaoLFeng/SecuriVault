package com.xlf.securivault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Springboot启动
 *
 * @author xiao_lfeng
 * @since v1.0.0
 * @version v1.0.0
 */
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class SecuriVaultApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecuriVaultApplication.class, args);
    }

}
