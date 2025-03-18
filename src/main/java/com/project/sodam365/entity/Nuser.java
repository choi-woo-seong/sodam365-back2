package com.project.sodam365.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "`user`")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Nuser extends BaseTimeEntity {
    @Id
    @Column(name = "n_userid", length = 50, nullable = false, updatable = false)
    @JsonProperty("nUserid")  // ✅ JSON 매핑을 명확히 지정
    private String nUserid;

    @Column(name = "n_password", length = 100, nullable = false)
    @JsonProperty("nPassword")  // ✅ JSON 매핑을 명확히 지정
    private String nPassword;

    @Column(name = "n_name", length = 30, nullable = false)
    @JsonProperty("nName")
    private String nName;

    @Column(name = "n_email", length = 30)
    @JsonProperty("nEmail")
    private String nEmail;

    @Column(name = "address", length = 100, nullable = false)
    @JsonProperty("address")
    private String address;

    @Column(name = "n_phone1", length = 20, nullable = false)
    @JsonProperty("nPhone1")
    private String nPhone1;

    @Column(name = "n_phone2", length = 20, nullable = true)
    @JsonProperty("nPhone2")
    private String nPhone2;


    @PrePersist
    public void generateId() {
        if (this.nUserid == null || this.nUserid.isEmpty()) {
            this.nUserid = UUID.randomUUID().toString();
        }
    }

    public void encodePassword(String encodedPassword) {
        this.nPassword = encodedPassword;
    }
}
