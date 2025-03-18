package com.project.sodam365.controller;

import com.project.sodam365.entity.Nuser;
import com.project.sodam365.entity.User;
import com.project.sodam365.repository.NuserRepository;
import com.project.sodam365.repository.UserRepository;
import com.project.sodam365.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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

    // 🔹 일반 회원 가입 (성공 시 true, 실패 시 false)
    @PostMapping("/register/nuser")
    public ResponseEntity<Map<String, Object>> registerNuser(@RequestBody Nuser nuser) {
        Map<String, Object> response = new HashMap<>();

        if (nuserRepository.findByNUserid(nuser.getNUserid()).isPresent()) {
            response.put("success", false);
            response.put("error", "User ID already exists");
            return ResponseEntity.badRequest().body(response);
        }

        String encryptedPassword = passwordEncoder.encode(nuser.getNPassword());

        Nuser user = Nuser.builder()
                .nUserid(nuser.getNUserid())
                .nPassword(encryptedPassword)
                .nName(nuser.getNName())
                .nEmail(nuser.getNEmail())
                .address(nuser.getAddress())
                .nPhone1(nuser.getNPhone1())
                .nPhone2(nuser.getNPhone2())
                .build();

        nuserRepository.save(user);
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    // 🔹 비즈니스 회원 가입 (성공 시 true, 실패 시 false)
    @PostMapping("/register/buser")
    public ResponseEntity<Map<String, Object>> registerBuser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();

        if (userRepository.findByUserid(user.getUserid()).isPresent()) {
            response.put("success", false);
            response.put("error", "Business ID already exists");
            return ResponseEntity.badRequest().body(response);
        }

        // 🔥 회원가입 시 비밀번호 암호화 확인
        System.out.println("🔍 암호화 전 비밀번호: " + user.getPassword());

        String encryptedPassword = passwordEncoder.encode(user.getPassword());

        System.out.println("🔍 암호화된 비밀번호: " + encryptedPassword);

        User buser = User.builder()
                .userid(user.getUserid())
                .password(encryptedPassword) // 🔥 암호화된 비밀번호 저장
                .name(user.getName())
                .ownername(user.getOwnername())
                .ownernum(user.getOwnernum())
                .ownerloc(user.getOwnerloc())
                .email(user.getEmail())
                .phone1(user.getPhone1())
                .phone2(user.getPhone2())
                .build();

        userRepository.save(buser);
        response.put("success", true);
        return ResponseEntity.ok(response);
    }


    // 🔹 일반 회원 로그인 (JWT + 사용자 이름 반환)
    @PostMapping("/login/nuser")
    public ResponseEntity<Map<String, Object>> loginNuser(@RequestBody Nuser nuser) {
        Optional<Nuser> foundUser = nuserRepository.findByNUserid(nuser.getNUserid());
        Map<String, Object> response = new HashMap<>();

        if (foundUser.isPresent() && passwordEncoder.matches(nuser.getNPassword(), foundUser.get().getNPassword())) {
            String token = jwtUtil.generateToken(foundUser.get().getNUserid(), "nuser");

            response.put("success", true);
            response.put("token", token);
            response.put("name", foundUser.get().getNName());
            return ResponseEntity.ok(response);
        }

        response.put("success", false);
        response.put("error", "Invalid credentials");
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
                String token = jwtUtil.generateToken(foundUser.get().getUserid(), "buser");

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
