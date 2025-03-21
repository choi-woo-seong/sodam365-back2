package com.project.sodam365.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey SECRET_KEY;
    private final long EXPIRATION_TIME;

    public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expiration) {
        this.SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
        this.EXPIRATION_TIME = expiration;
    }

    public String generateToken(String id, String role) {
        // 🔥 여기서 "admin"인지 체크하고, role 값을 "admin"으로 올바르게 설정
        if ("admin".equals(id)) {  // userId가 "admin"이면 role을 강제로 "admin"으로 설정
            role = "admin";
        }

        return Jwts.builder()
                .setSubject(id)  // 🔹 user ID (예: "admin")
                .claim("role", role)  // 🔥 JWT에 role 값 저장
                .setIssuedAt(new Date())  // 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))  // 만료 시간
                .signWith(SECRET_KEY)  // 서명
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

    // 🔹 JWT에서 `userid` 추출 (Bearer 자동 제거)
    public String extractUsername(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // "Bearer " 제거
        }

        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // ✅ 토큰의 Subject에서 `userid` 반환
    }

    // 🔹 관리자(`admin`) 계정 여부 확인
    public boolean isAdmin(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // "Bearer " 제거
        }

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String role = claims.get("role", String.class); // role 값 가져오기
        return "admin".equals(getUserRole(token)); // ✅ role이 "admin"이면 true, 아니면 false
    }

    // 🔹 JWT에서 `role` 값 추출
    public String getUserRole(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // "Bearer " 제거
        }

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("role", String.class); // 🔹 role 값 반환
    }
}

