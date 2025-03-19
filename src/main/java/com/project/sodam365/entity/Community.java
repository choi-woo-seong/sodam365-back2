package com.project.sodam365.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "community")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Community {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String c_title;

    @Column(length = 1000, nullable = false)
    private String c_content;

    @Column(length = 50, nullable = false)
    private String authorName; // ✅ 작성자 이름 (User 또는 Nuser)

    @Column(length = 10, nullable = false)
    private String authorType; // ✅ "USER" 또는 "NUSER"

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;

    @ManyToOne
    @JoinColumn(name = "nuserid")
    private Nuser nuser;

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();


}
