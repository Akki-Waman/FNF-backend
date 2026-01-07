package com.sipl.ticket.core.dao.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserPrincipal implements UserDetails {

    private final Users user;

    public UserPrincipal(Users user) {
        this.user = user;
    }


    public Long getUserId() {
        return user.getId();
    }

    public String getDisplayName() {
        return buildFullName();
    }

    public Users getUser() {
        return user;
    }

    private String buildFullName() {
        StringBuilder name = new StringBuilder();

        if (user.getFirstName() != null) {
            name.append(user.getFirstName());
        }
        if (user.getMiddleName() != null && !user.getMiddleName().isBlank()) {
            name.append(" ").append(user.getMiddleName());
        }
        if (user.getLastName() != null) {
            name.append(" ").append(user.getLastName());
        }
        return name.toString().trim();
    }

    /* ======================
       UserDetails methods
       ====================== */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Agar roles future me add karne ho to yahin se return karna
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(user.getIsActive());
    }
}
