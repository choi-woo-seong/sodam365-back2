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

    public String generateToken(String id, String role, String name) {
        // 🔥 여기서 "admin"인지 체크하고, role 값을 "admin"으로 올바르게 설정
        if ("admin".equals(id)) {  // userId가 "admin"이면 role을 강제로 "admin"으로 설정
            role = "admin";
        }

        return Jwts.builder()
                .setSubject(id)
                .claim("role", role)
                .claim("name", name) // 🔥 이제 문제 없음!
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
    // 이름 뽑아내기
    public String extractName(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("name", String.class); // 🔥 name 필드 추출
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

