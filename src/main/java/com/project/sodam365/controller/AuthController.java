package com.project.sodam365.controller;

import com.project.sodam365.dto.NuserDto;
import com.project.sodam365.entity.Nuser;
import com.project.sodam365.entity.Role;
import com.project.sodam365.entity.User;
import com.project.sodam365.repository.NuserRepository;
import com.project.sodam365.repository.UserRepository;
import com.project.sodam365.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.project.sodam365.service.EmailVerificationService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final NuserRepository nuserRepository;
    private final UserRepository userRepository;
    private final EmailVerificationService emailVerificationService;

    // 🔹 일반 회원 가입
    @PostMapping("/register/nuser")
    public ResponseEntity<Map<String, Object>> registerNuser(@RequestBody NuserDto nuserDto) {
        Map<String, Object> response = new HashMap<>();

        // ✅ 이메일 인증 여부 확인
        if (!emailVerificationService.isEmailVerified(nuserDto.getEmail())) {
            response.put("success", false);
            response.put("error", "이메일 인증을 진행해주십시오.");
            return ResponseEntity.status(403).body(response);
        }

        if (nuserDto.getPassword() == null || nuserDto.getPassword().isBlank()) {
            response.put("success", false);
            response.put("error", "비밀번호는 필수 항목입니다.");
            return ResponseEntity.badRequest().body(response);
        }

        if (nuserRepository.findByNUserid(nuserDto.getN_userid()).isPresent()) {
            response.put("success", false);
            response.put("error", "User ID already exists");
            return ResponseEntity.badRequest().body(response);
        }

        String encryptedPassword = passwordEncoder.encode(nuserDto.getPassword());

        Nuser user = Nuser.builder()
                .nUserid(nuserDto.getN_userid())
                .nPassword(encryptedPassword)
                .nName(nuserDto.getName())
                .nEmail(nuserDto.getEmail())
                .address(nuserDto.getAddress())
                .nPhone1(nuserDto.getPhone1())
                .nPhone2(nuserDto.getPhone2())
                .role(Role.ROLE_USER)
                .build();

        nuserRepository.save(user);
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    // 🔹 비즈니스 회원 가입
    @PostMapping("/register/buser")
    public ResponseEntity<Map<String, Object>> registerBuser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();

        // ✅ 이메일 인증 여부 확인
        if (!emailVerificationService.isEmailVerified(user.getEmail())) {
            response.put("success", false);
            response.put("error", "이메일 인증을 진행해주십시오.");
            return ResponseEntity.status(403).body(response);
        }

        if (userRepository.findByUserid(user.getUserid()).isPresent()) {
            response.put("success", false);
            response.put("error", "Business ID already exists");
            return ResponseEntity.badRequest().body(response);
        }

        String encryptedPassword = passwordEncoder.encode(user.getPassword());

        User buser = User.builder()
                .userid(user.getUserid())
                .password(encryptedPassword)
                .name(user.getName())
                .ownername(user.getOwnername())
                .ownernum(user.getOwnernum())
                .ownerloc(user.getOwnerloc())
                .email(user.getEmail())
                .phone1(user.getPhone1())
                .phone2(user.getPhone2())
                .role(Role.ROLE_BUSER)
                .build();

        userRepository.save(buser);
        response.put("success", true);
        return ResponseEntity.ok(response);
    }


    // 🔹 일반 회원 로그인 (JWT + 사용자 이름 반환)
    @PostMapping("/login/nuser")
    public ResponseEntity<Map<String, Object>> loginNuser(@RequestBody NuserDto loginDto) {
        Map<String, Object> response = new HashMap<>();

        Optional<Nuser> foundUser = nuserRepository.findByNUserid(loginDto.getN_userid());

        if (foundUser.isPresent() &&
                passwordEncoder.matches(loginDto.getPassword(), foundUser.get().getNPassword())) {

            String token = jwtUtil.generateToken(
                    foundUser.get().getNUserid(), "nuser", foundUser.get().getNName());

            response.put("success", true);
            response.put("token", token);
            response.put("name", foundUser.get().getNName());
            return ResponseEntity.ok(response);
        }

        response.put("success", false);
        response.put("error", "아이디 또는 비밀번호가 일치하지 않습니다.");
        return ResponseEntity.status(401).body(response);
    }


    // 🔹 비즈니스 회원 로그인 (JWT + 사용자 이름 반환)
    @PostMapping("/login/buser")
    public ResponseEntity<Map<String, Object>> loginBuser(@RequestBody User user) {
        Optional<User> foundUser = userRepository.findByUserid(user.getUserid());
        Map<String, Object> response = new HashMap<>();

        if (foundUser.isPresent()) {
            String encodedPassword = foundUser.get().getPassword(); // 🔥 DB에 저장된 암호화된 비밀번호
            String rawPassword = user.getPassword(); // 🔥 사용자가 입력한 원래 비밀번호
            String storedOwnerNum = foundUser.get().getOwnernum(); // 🔥 DB에 저장된 사업자 번호
            String inputOwnerNum = user.getOwnernum(); // 🔥 사용자가 입력한 사업자 번호

            // 🔍 로그 출력 (디버깅용)
            System.out.println("🔍 입력된 비밀번호: " + rawPassword);
            System.out.println("🔍 DB 저장된 비밀번호: " + encodedPassword);
            System.out.println("🔍 입력된 사업자 번호: " + inputOwnerNum);
            System.out.println("🔍 DB 저장된 사업자 번호: " + storedOwnerNum);

            // 🔥 비밀번호 & 사업자 번호 일치 여부 확인
            if (passwordEncoder.matches(rawPassword, encodedPassword) && inputOwnerNum.equals(storedOwnerNum)) {
                String token = jwtUtil.generateToken(foundUser.get().getUserid(), "buser", foundUser.get().getName());

                response.put("success", true);
                response.put("token", token);
                response.put("name", foundUser.get().getName());
                response.put("ownernum", storedOwnerNum);
                return ResponseEntity.ok(response);
            } else {
                System.out.println("❌ 비밀번호 또는 사업자 번호 불일치!");
            }
        } else {
            System.out.println("❌ 사용자 ID 없음!");
        }

        response.put("success", false);
        response.put("error", "Invalid credentials");
        return ResponseEntity.status(401).body(response);
    }


}
