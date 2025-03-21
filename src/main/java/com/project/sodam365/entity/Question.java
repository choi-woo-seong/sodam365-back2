package com.project.sodam365.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "question")
public class Question extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user; // 사업자

    @ManyToOne
    @JoinColumn(name = "n_userid")
    private Nuser nuser; // 일반 사용자

    @JsonIgnore
    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL)
    private Answer answer;

    @Column(nullable = false)
    private boolean isAnswered = false;
}
