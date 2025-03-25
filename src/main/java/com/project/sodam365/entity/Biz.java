package com.project.sodam365.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "biz")
@Getter
@Setter
@NoArgsConstructor
public class Biz extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(length = 30, nullable = false)
    private String b_title;

    @Column(length = 500, nullable = false)
    private String b_contents;

    @Column(length = 20)
    private String b_price;

    @Column(length = 100, nullable = false)
    private String b_link;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    private User userid;

    @Builder // ✅ 생성자에 @Builder 추가
    public Biz(Long no, String b_title, String b_contents, String b_price, String b_link, User userid) {
        this.no = no;
        this.b_title = b_title;
        this.b_contents = b_contents;
        this.b_price = b_price;
        this.b_link = b_link;
        this.userid = userid;
    }

}
