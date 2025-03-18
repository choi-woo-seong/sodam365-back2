package com.project.sodam365.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "location")
@Getter
@Setter
public class Location extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(length = 100, nullable = false)
    private String loc;

    @Column(nullable = false)
    private double x_info;

    @Column(nullable = false)
    private double y_info;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    private User userid;

}
