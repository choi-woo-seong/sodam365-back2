package com.project.sodam365.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "b_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity {

    @Id
    @Column(name = "userid", length = 50, nullable = false, updatable = false)
    @JsonProperty("userid")
    private String userid;

    @Column(name = "password", length = 100, nullable = false)
    @JsonProperty("password")
    private String password;

    @Column(name = "name", length = 30, nullable = false)
    @JsonProperty("name")
    private String name;

    @Column(name = "ownername", length = 30, nullable = false)
    @JsonProperty("ownername")
    private String ownername;

    @Column(name = "ownernum", length = 20, nullable = false)
    @JsonProperty("ownernum")
    private String ownernum;

    @Column(name = "ownerloc", length = 100, nullable = false)
    @JsonProperty("ownerloc")
    private String ownerloc;

    @Column(name = "email", length = 30)
    @JsonProperty("b_email")
    private String email;

    @Column(name = "phone1", length = 20, nullable = false)
    @JsonProperty("phone1")
    private String phone1;

    @Column(name = "phone2", length = 20)
    @JsonProperty("phone2")
    private String phone2;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "userid", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    private List<Biz> bizList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    private List<Community> communities = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    private List<Recent> recents = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    private List<Favorite> favorites = new ArrayList<>();

    @PrePersist
    public void generateId() {
        if (this.userid == null || this.userid.isEmpty()) {
            this.userid = UUID.randomUUID().toString();
        }
    }

    public void encodePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}