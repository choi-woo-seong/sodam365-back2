package com.project.sodam365.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "recent")
@Getter
@Setter
public class Recent extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long r_no;

    @Column(length = 30, nullable = false)
    private String r_title;

    @Column(length = 50)
    private String img;

    @ManyToOne
    @JoinColumn(name = "n_userid", nullable = false)
    private Nuser n_user;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    private User userid;

}
