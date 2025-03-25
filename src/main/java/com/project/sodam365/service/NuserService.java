package com.project.sodam365.service;

import com.project.sodam365.dto.NuserDto;
import com.project.sodam365.entity.Nuser;
import com.project.sodam365.repository.NuserRepository;
import com.project.sodam365.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NuserService {
    private final NuserRepository nuserRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Spring에서 제공하는 비밀번호 암호화

    // 🔹 로그인 검증 (예외 처리 개선 및 Optional 사용)
    public boolean login(String userid, String rawPassword) {
        Optional<Nuser> optionalUser = nuserRepository.findById(userid);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("사용자를 찾을 수 없습니다: " + userid);
        }

        Nuser nuser = optionalUser.get();

        // 비밀번호 암호화 비교
        return passwordEncoder.matches(rawPassword, nuser.getNPassword());
    }

    // 🔹 모든 사용자 조회
    public List<Nuser> getAllUsers() {
        return nuserRepository.findAll();
    }

    // 🔹 새로운 사용자 생성 (비밀번호 암호화 포함)
    public void createUser(NuserDto dto) {
        if (dto.getN_userid() == null || dto.getN_password() == null) {
            throw new IllegalArgumentException("User ID와 Password는 필수 입력값입니다.");
        }

        // 비밀번호 암호화 적용
        String encryptedPassword = passwordEncoder.encode(dto.getN_password());

        // DTO → 엔티티 변환 (빌더 패턴 사용)
        Nuser user = Nuser.builder()
                .nUserid(dto.getN_userid())
                .nPassword(encryptedPassword) // 암호화된 비밀번호 저장
                .nName(dto.getN_name())
                .nEmail(dto.getN_email())
                .address(dto.getAddress())
                .nPhone1(dto.getN_phone1())
                .nPhone2(dto.getN_phone2())
                .build();

        nuserRepository.save(user);
    }

    // 일반 회원 아이디 중복 확인
    public boolean isNUserIdDuplicate(String nUserid) {
        return nuserRepository.existsBynUserid(nUserid);
    }


    // 🔹 아이디 존재 여부 확인
    public boolean existsById(String nUserid) {
        return nuserRepository.existsById(nUserid);
    }

    public boolean isUserIdDuplicateAcrossAll(String id) {
        boolean isInUser = userRepository.findByUserid(id).isPresent();
        boolean isInNuser = nuserRepository.findByNUserid(id).isPresent();
        return isInUser || isInNuser;
    }

    public NuserDto getUserInfo(String nUserid) {
        return nuserRepository.findByNUserid(nUserid)
                .map(NuserDto::fromUserEntity)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    public void updateUser(String nUserid, Map<String, Object> info) {
        Nuser nuser = nuserRepository.findByNUserid(nUserid)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

//        nuser.setNName((String) info.get("nName"));
        nuser.setNEmail((String) info.get("nEmail"));
        nuser.setNPhone1((String) info.get("nPhone1"));
        nuser.setNPhone2((String) info.get("nPhone2"));
        nuser.setAddress((String) info.get("address"));

        nuserRepository.save(nuser);
    }

    public void deleteUser(String nUserid) {
        Nuser nuser = nuserRepository.findByNUserid(nUserid)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        nuserRepository.delete(nuser);
    }

    public void changePassword(String nUserid, String currentPassword, String newPassword) {
        Nuser nuser = nuserRepository.findByNUserid(nUserid)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));


        if (!passwordEncoder.matches(currentPassword, nuser.getNPassword())) {
            throw new RuntimeException("현재 비밀번호가 일치하지 않습니다.");
        }

        nuser.setNPassword(passwordEncoder.encode(newPassword));
        nuserRepository.save(nuser);
    }

}
