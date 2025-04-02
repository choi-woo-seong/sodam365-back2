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
import java.util.Map;

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
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        String encryptedPassword = passwordEncoder.encode(dto.getPassword());

        // DTO → 엔티티 변환
        Nuser user = Nuser.builder()
                .nUserid(dto.getN_userid())
                .nPassword(encryptedPassword)
                .nName(dto.getName())
                .nEmail(dto.getEmail())
                .address(dto.getAddress())
                .nPhone1(dto.getPhone1())
                .build();

        nuserRepository.save(user);
    }

    public boolean existsById(String userid) {
        return userRepository.existsById(userid);
    }


    // 비즈니스 회원 아이디 중복 확인
    public boolean isUserIdDuplicate(String userid) {
        return userRepository.existsByUserid(userid);
    }

    public boolean isUserIdDuplicateAcrossAll(String id) {
        boolean isInUser = userRepository.findByUserid(id).isPresent();
        boolean isInNuser = nuserRepository.findByNUserid(id).isPresent();
        return isInUser || isInNuser;
    }

    public UserDto getUserInfo(String userid) {
        return userRepository.findByUserid(userid)
                .map(UserDto::fromUserEntity)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    public void updateUser(String userid, Map<String, Object> info) {
        User user = userRepository.findByUserid(userid)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

//        user.setName((String) info.get("name"));
        user.setEmail((String) info.get("email"));
        user.setPhone1((String) info.get("phone1"));
        user.setPhone2((String) info.get("phone2"));
//        user.setOwnername((String) info.get("ownername"));
        user.setOwnernum((String) info.get("ownernum"));
        user.setOwnerloc((String) info.get("ownerloc"));

        userRepository.save(user);
    }

    public void deleteUser(String userid) {
        User user = userRepository.findByUserid(userid)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        userRepository.delete(user);
    }


    public void changePassword(String userid, String currentPassword, String newPassword) {
        User user = userRepository.findByUserid(userid)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        System.out.println("🔐 사용자가 입력한 비밀번호: " + currentPassword);
        System.out.println("🔐 DB 저장된 암호화 비밀번호: " + user.getPassword());
        System.out.println("✅ 일치 여부: " + passwordEncoder.matches(currentPassword, user.getPassword()));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("현재 비밀번호가 일치하지 않습니다.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }


}
