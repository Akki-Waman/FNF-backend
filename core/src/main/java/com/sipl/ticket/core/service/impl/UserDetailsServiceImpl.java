package com.sipl.ticket.core.service.impl;

import com.sipl.ticket.core.dao.entity.Users;
import com.sipl.ticket.core.dao.repository.UsersRepository;
import com.sipl.ticket.core.dto.response.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UsersRepository userMasterRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("[loadUserByUsername] Loading info by username : {}", username);
        Optional<Users> user = userMasterRepository.findByUserName(username);

        if (user.isPresent()) {
            Users userEntity = user.get();
            return new CustomUserDetails(
                    userEntity.getUserName(),
                    userEntity.getPassword(),
                    userEntity.getId()
            );
        }
        throw new UsernameNotFoundException("User not found: " + username);
    }
}
