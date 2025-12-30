package com.sipl.ticket.core.config;

import com.sipl.client.dms.impl.DocumentClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class DmsConfig {

    @Value("${dms.service.url:#{NULL}}")
    private String dmsServiceUrl;

    @Value("${application.id:Unknown-01}")
    private String applicationId;

    @Bean
    public DocumentClientService documentClientService() {
        return new DocumentClientService.Builder(dmsServiceUrl)
                .uploadEndpoint("/api/v1/documents/upload/")
                .downloadEndpoint("/api/v1/documents/download/")
                .deleteEndpoint("/api/v1/documents/delete/")
                .getEndpoint("/api/v1/documents/info/")
                .applicationId("app001")
                .build();
    }
}
