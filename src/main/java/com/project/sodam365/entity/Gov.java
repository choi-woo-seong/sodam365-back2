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

    @Column(length = 30, nullable = false)
    private String g_title;

    @Column(length = 20, nullable = false)
    private String financial_product;

    @Column(length = 10, nullable = false)
    private String irt;

    @Column(length = 10, nullable = false)
    private String lnLmt;

    @Column(length = 20, nullable = false)
    private String trgt;

    @Column(length = 50, nullable = false)
    private String usge;

    @Column(length = 20, nullable = false)
    private String hdlInst;

    @Column(length = 20, nullable = false)
    private String maxTotLnTrm;

    @Column(length = 20, nullable = false)
    private String rdptMthd;

}
