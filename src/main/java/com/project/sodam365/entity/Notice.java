package com.project.sodam365.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "notice")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ✅ 자동 증가 + 기본 키
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "n_title", nullable = false)
    private String n_title;  // 공지 제목

    @Column(name = "n_content", nullable = false)
    private String n_content; // 공지 내용

    @Column(name = "user", nullable = false)
    private String user; // 작성자 (admin)

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 작성일 자동 생성

    public Notice(String n_title, String n_content, String user) {
        this.n_title = n_title;
        this.n_content = n_content;
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }

    public void update(String n_title, String n_content) {
        this.n_title = n_title;
        this.n_content = n_content;
    }
}
