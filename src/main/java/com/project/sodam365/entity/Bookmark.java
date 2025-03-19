package com.project.sodam365.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "bookmark")
@Getter
@Setter
public class Bookmark extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(length = 100, nullable = false)
    private String link;

    @ManyToOne
    @JoinColumn(name = "n_userid", nullable = false)
    private Nuser n_user;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    private User userid;

}
