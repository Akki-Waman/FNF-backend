package com.sipl.ticket.core.config;

import net.bull.javamelody.MonitoringFilter;
import net.bull.javamelody.SessionListener;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JavaMelodyConfig {

    @Bean
    public FilterRegistrationBean<MonitoringFilter> javaMelodyFilter() {
        FilterRegistrationBean<MonitoringFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new MonitoringFilter());
        registration.addUrlPatterns("/*");
        registration.setName("javamelody");
        registration.setAsyncSupported(true);
        return registration;
    }

    @Bean
    public ServletListenerRegistrationBean<SessionListener> javaMelodyListener() {
        return new ServletListenerRegistrationBean<>(new SessionListener());
    }
}

