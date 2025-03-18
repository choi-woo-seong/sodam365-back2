package com.project.sodam365.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "product")
@Getter
@Setter
public class Product extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(length = 30, nullable = false)
    private String p_title;

    @Column(length = 50, nullable = false)
    private String p_contents;

    @Column(length = 50)
    private String img;

    @Column(length = 100, nullable = false)
    private String p_link;

    @ManyToOne
    @JoinColumn(name = "n_userid", nullable = false)
    private Nuser n_user;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    private User userid;
}
