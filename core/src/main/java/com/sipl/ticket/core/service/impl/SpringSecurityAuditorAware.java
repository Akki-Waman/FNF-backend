package com.sipl.ticket.core.service.impl;


import com.sipl.ticket.core.dao.entity.Users;
import com.sipl.ticket.core.dao.repository.UsersRepository;
import com.sipl.ticket.core.dto.response.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Slf4j
public class SpringSecurityAuditorAware implements AuditorAware<Users> {

    @Autowired
    private UsersRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Users> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof CustomUserDetails) {
            Long userId = ((CustomUserDetails) principal).getUserId();

            // ✅ IMPORTANT: No DB call, no transient object
            Users userRef = entityManager.getReference(Users.class, userId);

            return Optional.of(userRef);
        }

        return Optional.empty();
    }

    private Optional<Users> getDefaultAuditor() {
        return userRepository.findById(1L);
    }
}

