package com.project.sodam365.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "recent")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Recent extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long r_no;

    private Long postNo; // ✅ 최근 본 글의 번호
    private String category; // ✅ 최근 본 글의 카테고리
    private String title; // ✅ 최근 본 글 제목

    private String userid; // ✅ 사용자 ID (String으로도 유지)
    private String nuserid; // ✅ 일반 사용자 ID

    private LocalDateTime viewedAt; // ✅ 최근 본 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nuserid", insertable = false, updatable = false)
    private Nuser nuser;
}