package com.project.sodam365.security;

import com.project.sodam365.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = request.getHeader("Authorization");
        String uri = request.getRequestURI();

        // ✅ 인증 없이 통과시킬 경로
        if (uri.startsWith("/api/search/all") ||
                uri.startsWith("/api/notice/searchAll") ||
                uri.startsWith("/api/gov/fetch") ||
                uri.startsWith("/api/question/searchAll") ||
                uri.startsWith("/email/") ||
                uri.startsWith("/auth/")) {

            filterChain.doFilter(request, response);
            return;
        }

        // ✅ Authorization 헤더 처리
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // "Bearer " 제거

            // ✅ null, 빈 문자열, undefined 방지
            if (token.equals("null") || token.equals("undefined") || token.isBlank()) {
                filterChain.doFilter(request, response); // 그냥 통과
                return;
            }

            if (jwtUtil.validateToken(token)) {
                Claims claims = jwtUtil.getClaimsFromToken(token);
                String username = claims.getSubject();
                String role = claims.get("role", String.class);

                UserDetails userDetails = new User(username, "", Collections.emptyList());
                JwtAuthentication authentication = new JwtAuthentication(userDetails, role);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired JWT token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
