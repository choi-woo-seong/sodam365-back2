package com.project.sodam365.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.sodam365.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product extends BaseTimeEntity { // ✅ `BaseTimeEntity`에서 상속받아야 `createdAt`이 존재
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(length = 30, nullable = false)
    private String p_title;

    @Column(length = 50, nullable = false)
    private String p_contents;

    @Column(length = 1000, nullable = false)
    private String p_link;

    @Column(nullable = false)
    private String p_price;

    @ManyToOne
    @JoinColumn(name = "userid", referencedColumnName = "userid", nullable = false)
    @JsonBackReference
    private User user;
}
