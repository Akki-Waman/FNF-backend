package com.sipl.ticket.core.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
public class JwtUtil {
    public static final String SECRET = "wn0GSOkq62sJ4IgnX8W1nNLpqpJ8apFOIX2Wv3ZszQETj1R2Zq";
    private final int TOKEN_TIMEOUT = (1000 * 60 * 30);


    public String generateToken(String username,HashMap<String,Boolean> urlList) {
        Map<String, Object> claims = new HashMap<>();
        // HashMap<String,Boolean> dummyUrlList= generateDummyUrlList();
        claims.put("urlList", urlList);
        log.info("[generateToken] Generating token with username: {}", username);
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String username) {
        log.info("[createToken] Generating token with username and claim : {}", username);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_TIMEOUT))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        log.info("[getSignKey] Getting sign in key");   //ignore
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        String username = extractClaim(token, Claims::getSubject);
        log.info("Username from token is : {}", username);
        return username;
    }

    public Date extractExpiration(String token) {
        Date date = extractClaim(token, Claims::getExpiration);
        log.info("Token will be expired at : {}", date);  //ignore
        return date;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());   //ignore
    }


    public Boolean validateToken(String token, UserDetails userDetails, HttpServletRequest request) {
        final String username = extractUsername(token);
        Map<String, Object> claims = extractAllClaims(token);
        HashMap<String, Boolean> urlList = (HashMap<String, Boolean>) claims.get("urlList");
        String requestURL = request.getRequestURI();

        AntPathMatcher pathMatcher = new AntPathMatcher();

        Boolean valueMappedAgainstURL = null;

        for (String urlPattern : urlList.keySet()) {

            if (pathMatcher.match(urlPattern, requestURL)) {

                valueMappedAgainstURL = urlList.get(urlPattern);

                break;
            }
        }
        return  (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && valueMappedAgainstURL!=null);
    }
}