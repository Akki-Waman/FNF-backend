package com.sipl.ticket.core.filter;

import com.sipl.ticket.core.service.impl.UserDetailsServiceImpl;
import com.sipl.ticket.core.util.JwtUtil;
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
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("[doFilterInternal] validating request");
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        HttpRequestWrapper requestWrapper=null;

        if (authHeader != null) {
            authHeader = !authHeader.contains("Bearer ") ? "Bearer " + authHeader : authHeader;
            token = authHeader.substring(7).trim();
            username = jwtUtil.extractUsername(token);
            log.info("[doFilterInternal] Username value is: {}", username);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (Boolean.TRUE.equals(jwtUtil.validateToken(token, userDetails,request))) {
                requestWrapper = new HttpRequestWrapper(request);
                requestWrapper.addHeader("username", userDetails.getUsername());
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        if(requestWrapper==null){
            log.info(">>>>>>>RequestWrapper is null");
            filterChain.doFilter(request, response);
        }else{
            log.info(">>>>>>>>RequestWrapper populated ");
            filterChain.doFilter(requestWrapper, response);
        }
    }
}


class HttpRequestWrapper extends HttpServletRequestWrapper{

    Map<String,String> customHeaders = new HashMap<>();

    public HttpRequestWrapper(HttpServletRequest req){
        super(req);
    }

    public void addHeader(String name, String value) {
        customHeaders.put(name, value);
    }
    @Override
    public String getHeader(String name) {
        if(customHeaders.containsKey(name)){
            return customHeaders.get(name);
        }else{
            return super.getHeader(name);
        }
    }

}




