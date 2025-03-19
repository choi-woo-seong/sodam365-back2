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
    private String userid; // ✅ 최근 본 글을 본 사용자 ID
    private String nuserid;// 최근 본 글을 본 일반사용자 ID

    private LocalDateTime viewedAt; // ✅ 최근 본 시간
}