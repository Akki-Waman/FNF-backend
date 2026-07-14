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
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenHelper jwtTokenHelper;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestPath = request.getServletPath();
        log.debug("Processing request security layer for path: {}", requestPath);

        // 1. High-Performance Fast Check for Public Routes
        if (isPublicEndpoint(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");
        String jwtToken = null;
        String username = null;

        // 2. Validate Authorization Header Structure Safely
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
            try {
                username = jwtTokenHelper.getUsernameFromToken(jwtToken);
            } catch (Exception ex) {
                log.error("Failed to parse identity username claims from JWT token structure", ex);
            }
        } else {
            log.warn("Missing or improperly structured Authorization Header for protected resource: {}", requestPath);
        }

        // 3. Process Authentication Filter Execution Gate
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Enforce exact signature & temporal validity constraints
                if (jwtTokenHelper.validateToken(jwtToken, userDetails.getUsername())) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    log.debug("Authentication transaction context successfully set for user: {}", username);
                }
            } catch (Exception ex) {
                log.error("Authentication mapping context evaluation error for user: {}", username, ex);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid security credentials session token.");
                return;
            }
        }

        // Proceed down the execution filter chain safely
        filterChain.doFilter(request, response);
    }

    /**
     * Strict path verification logic preventing parameter manipulation bypass vulnerabilities.
     */
    private boolean isPublicEndpoint(String requestPath) {
        return requestPath.equals("/api/v1/auth/send-otp")
                || requestPath.equals("/api/v1/auth/verify-otp")
                || requestPath.startsWith("/swagger-ui")
                || requestPath.startsWith("/swagger-resources")
                || requestPath.startsWith("/v3/api-docs")
                || requestPath.startsWith("/webjars");
    }
}