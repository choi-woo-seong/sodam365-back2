package com.project.sodam365.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "community")
@Getter
@Setter
public class Community extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(length = 30, nullable = false)
    private String c_title;

    @Column(length = 500, nullable = false)
    private String c_contents;

    @Column(length = 500)
    private String c_comment;

    @ManyToOne
    @JoinColumn(name = "n_userid", nullable = false)
    private Nuser n_user;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    private User userid;

}
