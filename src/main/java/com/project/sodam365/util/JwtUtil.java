package com.project.sodam365.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey SECRET_KEY;
    private final long EXPIRATION_TIME;

    public JwtUtil() {
        String secretEnv = System.getenv("JWT_SECRET");
        String expirationEnv = System.getenv("JWT_EXPIRATION");

        if (secretEnv == null || expirationEnv == null) {
            throw new IllegalArgumentException("환경변수 JWT_SECRET 또는 JWT_EXPIRATION이 설정되지 않았습니다.");
        }

        this.SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretEnv));
        this.EXPIRATION_TIME = Long.parseLong(expirationEnv);
    }

    public String generateToken(String id, String role, String name) {
        if ("admin".equals(id)) {
            role = "admin";
        }

        return Jwts.builder()
                .setSubject(id)
                .claim("role", role)
                .claim("name", name)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        return getClaimsFromToken(token).getSubject();
    }

    public String extractName(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        return getClaimsFromToken(token).get("name", String.class);
    }

    public boolean isAdmin(String token) {
        return "admin".equals(getUserRole(token));
    }

    public String getUserRole(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        return getClaimsFromToken(token).get("role", String.class);
    }

    public String extractUserType(String token) {
        return getUserRole(token);
    }

    public String extractUserId(String token) {
        return extractUsername(token);
    }
}
