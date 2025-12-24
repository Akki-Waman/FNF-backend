package com.sipl.ticket.core.config;

import com.sipl.ticket.core.dao.entity.Users;
import com.sipl.ticket.core.service.impl.SpringSecurityAuditorAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Slf4j
@Configuration
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
public class JpaAuditingConfiguration {
    @Bean
    public AuditorAware<Users> springSecurityAuditorAware() {
        log.debug("Creating Audit Aware instance for JPA Auditing");
        return new SpringSecurityAuditorAware();
    }
}

