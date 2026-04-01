package com.sipl.ticket.core.util;

import com.sipl.ticket.core.dao.entity.UserPrincipal;
import com.sipl.ticket.core.dao.entity.Users;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    private SecurityUtil() {}

    public static Long getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return null;

        UserPrincipal p = (UserPrincipal) auth.getPrincipal();
        return p.getUserId();
    }

    public static Users getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) principal;
            return userPrincipal.getUser();
        }

        return null;
    }

    public static String getStaffName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return "SYSTEM";

        UserPrincipal p = (UserPrincipal) auth.getPrincipal();
        return p.getDisplayName(); // e.g. "Technology Support"
    }
}
