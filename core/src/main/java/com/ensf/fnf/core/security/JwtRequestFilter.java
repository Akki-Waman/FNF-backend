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

    private final JwtTokenHelper
            jwtTokenHelper;

    private final CustomUserDetailsService
            userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException,
            IOException {

        String requestPath =
                request.getServletPath();

        log.info(
                "Request Path : {}",
                requestPath
        );

        if (isPublicEndpoint(
                requestPath
        )) {

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }

        String authorizationHeader =
                request.getHeader(
                        "Authorization"
                );

        String jwtToken = null;
        String username = null;

        if (authorizationHeader != null
                && authorizationHeader.startsWith(
                "Bearer ")) {

            jwtToken =
                    authorizationHeader.substring(
                            7
                    );

            try {

                username =
                        jwtTokenHelper
                                .getUsernameFromToken(
                                        jwtToken
                                );

                log.info(
                        "JWT Username : {}",
                        username
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

            try {

                UserDetails userDetails =
                        userDetailsService
                                .loadUserByUsername(
                                        username
                                );

                if (jwtTokenHelper
                        .validateToken(
                                jwtToken
                        )) {

                    UsernamePasswordAuthenticationToken
                            authentication =
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

                    log.info(
                            "Authentication Success : {}",
                            username
                    );
                }

            } catch (Exception ex) {

                log.error(
                        "Authentication Failed : {}",
                        username,
                        ex
                );
            }
        }

        filterChain.doFilter(
                request,
                response
        );
    }

    private boolean isPublicEndpoint(
            String requestPath) {

        return requestPath.contains("/auth/send-otp")
                || requestPath.contains("/auth/verify-otp")
                || requestPath.contains("/swagger-ui")
                || requestPath.contains("/swagger-resources")
                || requestPath.contains("/v3/api-docs")
                || requestPath.contains("/webjars");
    }
}