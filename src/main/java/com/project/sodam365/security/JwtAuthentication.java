package com.project.sodam365.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class JwtAuthentication extends AbstractAuthenticationToken {
    private final UserDetails principal;
    private final String role;

    public JwtAuthentication(UserDetails principal, String role) {
        super(null);
        this.principal = principal;
        this.role = role;
        setAuthenticated(true); // JWT가 검증되었으므로 true 설정
    }

    @Override
    public Object getCredentials() {
        return null; // JWT는 비밀번호를 포함하지 않음
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public String getRole() {
        return role;
    }
}
