package com.project.sodam365.service;

import com.project.sodam365.controller.UserController;
import com.project.sodam365.dto.NuserDto;
import com.project.sodam365.dto.UserDto;
import com.project.sodam365.entity.Nuser;
import com.project.sodam365.entity.User;
import com.project.sodam365.repository.NuserRepository;
import com.project.sodam365.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final NuserRepository nuserRepository;
    private final PasswordEncoder passwordEncoder;

    // 로그인 검증
    public boolean login(String userid, String rawPassword) {
        User user = userRepository.findById(userid)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        // 입력된 비밀번호(rawPassword)와 저장된 암호화된 비밀번호 비교
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    // 모든 사용자 조회
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 새로운 사용자 생성 (비밀번호 암호화 포함)
    public void createUser(UserDto dto) {
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        String encryptedPassword = passwordEncoder.encode(dto.getPassword());

        // DTO → 엔티티 변환
        User user = User.builder()
                .userid(dto.getUserid())
                .password(encryptedPassword)
                .name(dto.getName())
                .ownername(dto.getOwnername())
                .ownernum(dto.getOwnernum())
                .ownerloc(dto.getOwnerloc())
                .email(dto.getEmail())
                .phone1(dto.getPhone1())
                .phone2(dto.getPhone2())
                .build();

        userRepository.save(user);
    }

    public void createNuser(NuserDto dto) {
        if (dto.getN_password() == null || dto.getN_password().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        String encryptedPassword = passwordEncoder.encode(dto.getN_password());

        // DTO → 엔티티 변환
        Nuser user = Nuser.builder()
                .nUserid(dto.getN_userid())
                .nPassword(encryptedPassword)
                .nName(dto.getN_name())
                .nEmail(dto.getN_email())
                .address(dto.getAddress())
                .nPhone1(dto.getN_phone1())
                .build();

        nuserRepository.save(user);
    }

    public boolean existsById(String userid) {
        return userRepository.existsById(userid);
    }
}
