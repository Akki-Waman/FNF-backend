package com.ensf.fnf.core.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY =
            "ENSF_FNF_SECRET_KEY_2025_ENERSOFTRICAL_PRIVATE_KEY";

    private static final long EXPIRATION_TIME =
            24 * 60 * 60 * 1000;

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(
                        StandardCharsets.UTF_8
                )
        );
    }

    public String generateToken(
            String mobileNumber) {

        return Jwts.builder()
                .setSubject(
                        mobileNumber
                )
                .setIssuedAt(
                        new Date()
                )
                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + EXPIRATION_TIME
                        )
                )
                .signWith(
                        getSigningKey(),
                        SignatureAlgorithm.HS256
                )
                .compact();
    }

    public String getMobileNumber(
            String token) {

        Claims claims =
                Jwts.parserBuilder()
                        .setSigningKey(
                                getSigningKey()
                        )
                        .build()
                        .parseClaimsJws(
                                token
                        )
                        .getBody();

        return claims.getSubject();
    }

    public boolean isTokenExpired(
            String token) {

        Claims claims =
                Jwts.parserBuilder()
                        .setSigningKey(
                                getSigningKey()
                        )
                        .build()
                        .parseClaimsJws(
                                token
                        )
                        .getBody();

        return claims.getExpiration()
                .before(
                        new Date()
                );
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
}