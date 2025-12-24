package com.sipl.ticket.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.sipl.notification.service.impl.Notification;

import java.net.UnknownHostException;


@Configuration
public class NotificationConfig {

    @Value("${notification.service.url}")
    private String url;

    @Bean
    public Notification notification() throws UnknownHostException {
        Notification notification = new Notification(url);
        return notification;
    }
}
