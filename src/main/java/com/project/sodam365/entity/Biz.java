package com.project.sodam365.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "biz")
@Getter
@Setter
public class Biz extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(length = 30, nullable = false)
    private String b_title;

    @Column(length = 500, nullable = false)
    private String b_contents;

    @Column(length = 50)
    private String img;

    @Column(length = 20)
    private String b_price;

    @Column(length = 100, nullable = false)
    private String b_link;

    @ManyToOne
    @JoinColumn(name = "n_userid", nullable = false)
    private Nuser n_user;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    private User userid;

}
