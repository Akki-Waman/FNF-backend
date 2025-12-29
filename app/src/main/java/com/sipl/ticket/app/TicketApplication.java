package com.sipl.ticket.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan({"com.sipl.ticket.core.dao.entity"})


@ComponentScan(basePackages = { "com.sipl.ticket" })
@EnableJpaRepositories({"com.sipl.ticket.core.dao.repository"})
//@PropertySource("classpath:core.properties")
/*@PropertySource("classpath:vehicle.properties")
@PropertySource("classpath:rtls.properties")*/
@EnableAsync
@EnableScheduling
@EnableCaching
public class TicketApplication {
    public static void main(String[] args) {
        SpringApplication.run(TicketApplication.class, args);
    }
}