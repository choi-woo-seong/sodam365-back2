package com.project.sodam365.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final StringRedisTemplate redisTemplate;
    private final JavaMailSender mailSender;

    // 인증 코드 전송
    public void sendVerificationCode(String email) {
        String code = String.format("%06d", new Random().nextInt(999999)); // 6자리 숫자

        // Redis에 저장 (TTL 10분)
        redisTemplate.opsForValue().set(getKey(email), code, 10, TimeUnit.MINUTES);

        // 이메일 전송
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("이메일 인증 코드입니다");
        message.setText("인증 코드: " + code + "\n10분 이내로 입력해주세요.");
        mailSender.send(message);
    }

    // 인증 코드 검증
    public boolean verifyCode(String email, String inputCode) {
        String storedCode = redisTemplate.opsForValue().get(getKey(email));
        return storedCode != null && storedCode.equals(inputCode);
    }

    // 인증 여부 확인
    public boolean isEmailVerified(String email) {
        return redisTemplate.hasKey(getKey(email)); // 코드가 남아 있으면 인증된 걸로 간주
    }

    private String getKey(String email) {
        return "email:verify:" + email;
    }
}
