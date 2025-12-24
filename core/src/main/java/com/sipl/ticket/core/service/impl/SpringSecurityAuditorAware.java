package com.sipl.ticket.core.service.impl;


import com.sipl.ticket.core.dao.entity.Users;
import com.sipl.ticket.core.dao.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
@Slf4j
public class SpringSecurityAuditorAware implements AuditorAware<Users> {

    @Autowired private UsersRepository userRepository;

    @Override
    public Optional<Users> getCurrentAuditor() {
        log.info("getCurrentAuditor(): Getting current Auditor");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            log.warn("Auth is null or unauthenticated.");
            //      return Optional.empty();
            return getDefaultAuditor();
        }

        Object principal = auth.getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = String.valueOf(principal);
        }

        log.info("Fetching auditor for username: {}", username);

        if ("anonymousUser".equalsIgnoreCase(username)) {
            String message = "User is not logged in. Please log in to continue.";
            log.error(message);
            // throw new IllegalArgumentException(message);
            return getDefaultAuditor();
        }
        try {
            return userRepository.findByUserName(username);
        } catch (Exception ex) {
            log.error("Error fetching Users in AuditAwareImpl: {}", ex.getMessage(), ex);
            // return Optional.empty();
            return getDefaultAuditor();
        }
    }

    private Optional<Users> getDefaultAuditor() {
        return userRepository.findById(1L);
    }
}

