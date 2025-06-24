package ru.t1.school.second_service.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
@Slf4j
public class JwtUtils {
    @Value("${security.jwt.secret}")
    private String jwtSecret;

    @Value("${security.jwt.expirationMs}")
    private Long jwtExpirationMs;

    private SecretKey key;
    private JwtParser jwtParser;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        jwtParser = Jwts.parser()
                .verifyWith(key)
                .build();
    }

    public String generateJwtToken(UserDetails principal) {
        return Jwts.builder()
                .subject(principal.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(jwtExpirationMs)))
                .signWith(key)
                .compact();
    }

    public String getUserIdFromJwtToken(String token) {
        return jwtParser
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String token) {
        try {
            jwtParser.parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
