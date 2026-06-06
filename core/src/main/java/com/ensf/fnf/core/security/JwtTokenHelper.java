package com.ensf.fnf.core.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenHelper {

    private static final String SECRET =
            "ENSF_FNF_SECRET_KEY_2025_ENERSOFTRICAL_PRIVATE_KEY";

    private static final long JWT_TOKEN_VALIDITY =
            24 * 60 * 60;

    public String generateToken(
            String email) {

        Map<String, Object> claims =
                new HashMap<>();

        return doGenerateToken(
                claims,
                email
        );
    }

    private String doGenerateToken(
            Map<String, Object> claims,
            String subject) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(
                        new Date()
                )
                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + JWT_TOKEN_VALIDITY * 1000
                        )
                )
                .signWith(
                        getSigningKey()
                )
                .compact();
    }

    public String getUsernameFromToken(
            String token) {

        return getClaimFromToken(
                token,
                Claims::getSubject
        );
    }

    public <T> T getClaimFromToken(
            String token,
            Function<Claims, T> resolver) {

        Claims claims =
                getAllClaimsFromToken(
                        token
                );

        return resolver.apply(
                claims
        );
    }

    private Claims getAllClaimsFromToken(
            String token) {

        return Jwts.parserBuilder()
                .setSigningKey(
                        getSigningKey()
                )
                .build()
                .parseClaimsJws(
                        token
                )
                .getBody();
    }

    public boolean validateToken(
            String token) {

        try {

            Jwts.parserBuilder()
                    .setSigningKey(
                            getSigningKey()
                    )
                    .build()
                    .parseClaimsJws(
                            token
                    );

            return true;

        } catch (Exception ex) {

            return false;
        }
    }

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                SECRET.getBytes()
        );
    }
}