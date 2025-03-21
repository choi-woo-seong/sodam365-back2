package com.project.sodam365.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "answer")
public class Answer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = true)
    private String a_title;

    @Column(length = 1000, nullable = false)
    private String a_contents;

    @Column(length = 1000, nullable = true)
    private String answer;

    @Column(nullable = false)
    private String adminId; // 관리자 ID 저장

    @OneToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
}
