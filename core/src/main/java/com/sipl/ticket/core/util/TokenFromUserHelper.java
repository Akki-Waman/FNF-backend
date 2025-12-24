package com.sipl.ticket.core.util;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class TokenFromUserHelper {

    private TokenFromUserHelper() {

    }

    public static String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(6);
        }
        return null;
    }
}
