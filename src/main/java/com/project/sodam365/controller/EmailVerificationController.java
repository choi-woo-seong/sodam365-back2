package com.project.sodam365.controller;

import com.project.sodam365.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    // 인증 코드 요청
    @PostMapping("/send-code")
    public ResponseEntity<?> sendCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            throw new IllegalArgumentException("유효하지 않은 이메일 주소입니다: " + email);
        }
        System.out.println(">>> 발송할 이메일: " + email);
        emailVerificationService.sendVerificationCode(email);
        return ResponseEntity.ok("인증 코드가 이메일로 전송되었습니다.");
    }

    // 인증 코드 검증
    @PostMapping("/verify-code")
    public ResponseEntity<Map<String, Object>> verifyCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");

        boolean success = emailVerificationService.verifyCode(email, code);
        Map<String, Object> response = new HashMap<>();

        if (success) {
            response.put("success", true);
            response.put("message", "이메일 인증 성공");
        } else {
            response.put("success", false);
            response.put("message", "인증 실패 또는 만료됨");
        }

        return ResponseEntity.ok(response); // ✅ 항상 200 OK로 응답
    }

}
