package com.project.sodam365.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "gov")
@Getter
@Setter
public class Gov extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(length = 1000, nullable = false)
    private String g_title;

    @Column(length = 1000)
    private String finPrdNm;

    @Column(length = 1000)
    private String irt;

    @Column(length = 1000)
    private String lnLmt;

    @Column(length = 1000)
    private String trgt;

    @Column(length = 1000)
    private String usge;

    @Column(length = 1000)
    private String hdlInst;

    @Column(length = 1000)
    private String maxTotLnTrm;

    @Column(length = 1000)
    private String rdptMthd;

}
