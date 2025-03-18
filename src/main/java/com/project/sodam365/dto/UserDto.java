package com.project.sodam365.dto;

import com.project.sodam365.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String userid;
    private String password;
    private String name;
    private String ownername;
    private String ownernum;
    private String ownerloc;
    private String email;
    private String phone1;
    private String phone2;

    // User 엔티티 → UserDto 변환
    public static UserDto fromUserEntity(User user) {
        return UserDto.builder()
                .userid(user.getUserid())
                .password(user.getPassword())
                .name(user.getName())
                .ownername(user.getOwnername())
                .ownernum(user.getOwnernum())
                .ownerloc(user.getOwnerloc())
                .email(user.getEmail())
                .phone1(user.getPhone1())
                .phone2(user.getPhone2())
                .build();
    }

    // UserDto → User 엔티티 변환 (빌더 패턴 적용)
    public static User fromUserDto(UserDto userDto) {
        return User.builder()
                .userid(userDto.getUserid())
                .password(userDto.getPassword())
                .name(userDto.getName())
                .ownername(userDto.getOwnername())
                .ownernum(userDto.getOwnernum())
                .ownerloc(userDto.getOwnerloc())
                .email(userDto.getEmail())
                .phone1(userDto.getPhone1())
                .phone2(userDto.getPhone2())
                .build();
    }
}
