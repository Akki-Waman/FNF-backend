package com.ensf.fnf.core.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter
        extends OncePerRequestFilter {

    private final JwtTokenHelper jwtTokenHelper;

    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException,
            IOException {

        String path =
                request.getServletPath();

        log.info(
                "Request Path : {}",
                path
        );

        /*
         * Swagger & Auth APIs Skip
         */
        if (path.contains("/swagger-ui")
                || path.contains("/v3/api-docs")
                || path.contains("/swagger-resources")
                || path.contains("/webjars")
                || path.contains("/api/v1/auth")) {

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }

        String requestToken =
                request.getHeader(
                        "Authorization"
                );

        String username = null;
        String token = null;

        if (requestToken != null
                && requestToken.startsWith(
                "Bearer ")) {

            token =
                    requestToken.substring(
                            7
                    );

            try {

                username =
                        jwtTokenHelper
                                .getUsernameFromToken(
                                        token
                                );

            } catch (Exception ex) {

                log.error(
                        "Invalid JWT Token",
                        ex
                );
            }

        } else {

            log.warn(
                    "Authorization Header Missing"
            );
        }

        if (username != null
                && SecurityContextHolder
                .getContext()
                .getAuthentication()
                == null) {

            UserDetails userDetails =
                    userDetailsService
                            .loadUserByUsername(
                                    username
                            );

            if (jwtTokenHelper
                    .validateToken(
                            token
                    )) {

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(
                                        request
                                )
                );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(
                                authentication
                        );
            }
        }

        filterChain.doFilter(
                request,
                response
        );
    }
}