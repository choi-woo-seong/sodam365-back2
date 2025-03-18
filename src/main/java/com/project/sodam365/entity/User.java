package com.project.sodam365.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "b_user")  // ✅ 테이블명 유지
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity {

    @Id
    @Column(name = "userid", length = 50, nullable = false, updatable = false)
    @JsonProperty("userid")  // ✅ JSON 매핑 명확화
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
    @JsonProperty("email")
    private String email;

    @Column(name = "phone1", length = 20, nullable = false)
    @JsonProperty("phone1")
    private String phone1;

    @Column(name = "phone2", length = 20)
    @JsonProperty("phone2")
    private String phone2;

    // 엔티티 저장 전, UUID 자동 생성
    @PrePersist
    public void generateId() {
        if (this.userid == null || this.userid.isEmpty()) {
            this.userid = UUID.randomUUID().toString();
        }
    }

    // 비밀번호 설정 (암호화 적용)
    public void encodePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
