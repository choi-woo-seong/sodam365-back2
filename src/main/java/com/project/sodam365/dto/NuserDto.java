package com.project.sodam365.dto;

import com.project.sodam365.entity.Nuser;
import com.project.sodam365.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NuserDto {
    private String n_userid;
    private String password;
    private String name;
    private String address;
    private String email;
    private String phone1;
    private String phone2;

    // User 엔티티 → UserDto 변환
    public static NuserDto fromUserEntity(Nuser user) {
        return NuserDto.builder()
                .n_userid(user.getNUserid())
                .password(user.getNPassword())
                .name(user.getNName())
                .email(user.getNEmail())
                .address(user.getAddress())
                .phone1(user.getNPhone1())
                .phone2(user.getNPhone2())
                .build();
    }

    // UserDto → User 엔티티 변환 (빌더 패턴 적용)
    public static Nuser fromUserDto(NuserDto userDto) {
        return Nuser.builder()
                .nUserid(userDto.getN_userid())
                .nPassword(userDto.getPassword())
                .nName(userDto.getName())
                .nEmail(userDto.getEmail())
                .address(userDto.getAddress())
                .nPhone1(userDto.getPhone1())
                .nPhone2(userDto.getPhone2())
                .build();
    }
}
