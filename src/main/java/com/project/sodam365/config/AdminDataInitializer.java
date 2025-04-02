package com.project.sodam365.config;

import com.project.sodam365.entity.Role;
import com.project.sodam365.entity.User;
import com.project.sodam365.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // 🔹 BCryptPasswordEncoder → PasswordEncoder 변경


    @Override
    public void run(String... args) {
        String adminId = "admin";
        String adminPassword = "admin"; // ✅ 기본 비밀번호 (변경 가능)
        String encryptedPassword = passwordEncoder.encode(adminPassword);

        // ✅ 이미 admin 계정이 있으면 추가하지 않음
        if (userRepository.findByUserid(adminId).isEmpty()) {
            User admin = User.builder()
                    .userid(adminId)
                    .password(encryptedPassword)
                    .name("관리자")
                    .ownername("관리자")
                    .ownernum("1111")
                    .ownerloc("본사")
                    .email("admin@example.com")
                    .phone1("010-0000-0000")
                    .phone2(null)
                    .role(Role.ROLE_ADMIN)
                    .build();

            userRepository.save(admin);
            System.out.println("✅ 관리자 계정이 생성되었습니다. (ID: admin, PW: admin)");
        } else {
            System.out.println("⚡ 관리자 계정이 이미 존재합니다.");
        }
    }
}
