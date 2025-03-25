package com.project.sodam365.config;

import com.project.sodam365.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // 🔥 CORS 설정 적용
                .csrf(csrf -> csrf.disable()) // 🔥 CSRF 보호 비활성화 (API 서버에서는 필요 없음)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 🔥 세션 사용 안 함 (JWT 기반 인증)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // 🔥 로그인 & 회원가입 엔드포인트는 인증 없이 접근 가능
                        .requestMatchers("/api/products/searchAll").permitAll() // ✅ 상품 조회 API 허용
                        .requestMatchers(HttpMethod.POST, "/api/products").authenticated() // 상품 등록은 인증 필요
                        .requestMatchers("/api/users/check-duplicate2").permitAll() // ✅ 중복 확인 API 허용
                        .requestMatchers("/api/users/check-duplicate").permitAll() // ✅ 중복 확인 API 허용
                        .requestMatchers("/api/products/productDetail/**").permitAll() //상품조회
                        .requestMatchers("/api/products/productUpdate/**").authenticated() //상품수정 인증필요
                        .requestMatchers(HttpMethod.DELETE, "/api/products/productDelete/**").authenticated() // ✅ 상품 삭제도 인증 필요
                        .requestMatchers(HttpMethod.POST, "/api/biz").authenticated() // 상품 등록은 인증 필요
                        .requestMatchers("/api/biz/searchAll").permitAll() //비즈니스 전체 조회
                        .requestMatchers("/api/biz/businessDetail/**").permitAll() //특정 비즈니스 조회
                        .requestMatchers("/api/community/searchAll").permitAll() //비즈니스 전체 조회
                        .requestMatchers("/api/community/communityDetail/**").permitAll() //비즈니스 특정 조회
                        .requestMatchers("/community/create", "/community/update/**", "/community/delete/**").authenticated() // 🔐 인증 필요
                        .requestMatchers("/api/comment/byCommunity/**").permitAll()
                        .requestMatchers("/api/comment/create", "/api/comment/update/**", "/api/comment/delete/**").authenticated()
                        .requestMatchers("/api/notice/create", "/api/notice/update", "/api/notice/delete").authenticated()
                        .requestMatchers("/api/notice/searchAll").permitAll()
                        .requestMatchers("/api/notice/noticeDetail/**").permitAll()
                        .requestMatchers("/api/question/create", "/api/question/update", "/api/question/delete").authenticated()
                        .requestMatchers("/api/question/searchAll").permitAll()
                        .requestMatchers("/api/question/questionDetail/**").permitAll()
                        .requestMatchers("/api/answer/byQuestion/**").permitAll()
                        .requestMatchers("/api/answer/create", "/api/answer/update", "/api/answer/delete").authenticated()
                        .requestMatchers("/api/answer/searchAll").permitAll()
                        .requestMatchers("/api/answer/answerDetail/**").permitAll()
                        .requestMatchers("/api/gov/fetch").permitAll()
                        .requestMatchers("/api/gov/**").permitAll()
                        .requestMatchers("/api/bookmark/toggle", "/api/bookmark/searchAll", "/api/bookmark/check").authenticated()
                        .requestMatchers("/api/bookmark/mapped").authenticated()
                        .requestMatchers("/api/mypage/posts").authenticated()
                        .requestMatchers("/api/main/save-recent").authenticated()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // 🔥 JWT 필터 적용

        return http.build();
    }

    // 🔹 CORS 설정 (모든 도메인에서 접근 가능)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // ✅ 쿠키 허용 (JWT 인증 시 필요)
        config.setAllowedOriginPatterns(List.of("*")); // ✅ 모든 도메인 허용 (setAllowedOrigins("*") 대신 사용)
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // ✅ 모든 HTTP 메서드 허용
        config.setAllowedHeaders(List.of("*")); // ✅ 모든 요청 헤더 허용
        config.setExposedHeaders(List.of("Authorization")); // ✅ 응답 헤더 공개

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // ✅ 모든 엔드포인트에 적용
        return source;
    }
}
