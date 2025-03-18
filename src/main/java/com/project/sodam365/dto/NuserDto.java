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
    private String n_password;
    private String n_name;
    private String address;
    private String n_email;
    private String n_phone1;
    private String n_phone2;

    // User 엔티티 → UserDto 변환
    public static NuserDto fromUserEntity(Nuser user) {
        return NuserDto.builder()
                .n_userid(user.getNUserid())
                .n_password(user.getNPassword())
                .n_name(user.getNName())
                .n_email(user.getNEmail())
                .address(user.getAddress())
                .n_phone1(user.getNPhone1())
                .n_phone2(user.getNPhone2())
                .build();
    }

    // UserDto → User 엔티티 변환 (빌더 패턴 적용)
    public static Nuser fromUserDto(NuserDto userDto) {
        return Nuser.builder()
                .nUserid(userDto.getN_userid())
                .nPassword(userDto.getN_password())
                .nName(userDto.getN_name())
                .nEmail(userDto.getN_email())
                .address(userDto.getAddress())
                .nPhone1(userDto.getN_phone1())
                .nPhone2(userDto.getN_phone2())
                .build();
    }
}
