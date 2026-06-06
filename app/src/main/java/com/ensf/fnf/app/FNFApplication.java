package com.ensf.fnf.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.ensf.fnf"
})
@EntityScan(basePackages = {
        "com.ensf.fnf.core.dao.entity"
})
@EnableJpaRepositories(basePackages = {
        "com.ensf.fnf.core.dao.repository"
})
@EnableAsync
@EnableScheduling
@EnableCaching
public class FNFApplication {

    public static void main(String[] args) {

        SpringApplication.run(
                FNFApplication.class,
                args
        );
    }
}