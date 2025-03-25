package com.project.sodam365.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "favorite")
@Getter
@Setter
public class Favorite extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String targetPgm;  // 예: "productDetail", "govDetail", "bizDetail"

    // 찜 대상 ID (product, gov, biz 중 하나의 ID)
    @Column(nullable = false)
    private Long targetId;

    // 찜 대상 타입
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FavoriteType targetType;

    // 일반 사용자 (nullable 가능)
    @ManyToOne
    @JoinColumn(name = "nuser_id")
    private Nuser nuser;

    // 사업자 사용자 (nullable 가능)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
