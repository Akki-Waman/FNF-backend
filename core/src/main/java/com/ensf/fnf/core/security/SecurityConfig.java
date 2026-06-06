package com.ensf.fnf.core.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtRequestFilter
            jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http
                .csrf()
                .disable()

                .authorizeRequests()

                .antMatchers(
                        "/api/v1/auth/**",

                        "/swagger-ui/**",
                        "/swagger-ui.html",

                        "/v2/api-docs",
                        "/v3/api-docs",
                        "/v3/api-docs/**",

                        "/swagger-resources/**",
                        "/webjars/**"
                )
                .permitAll()

                .anyRequest()
                .authenticated()

                .and()

                .sessionManagement()
                .sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS
                );

        http.addFilterBefore(
                jwtRequestFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }
}