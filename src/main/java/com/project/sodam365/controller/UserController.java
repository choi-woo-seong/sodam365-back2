package com.project.sodam365.controller;

import com.project.sodam365.dto.NuserDto;
import com.project.sodam365.dto.UserDto;
import com.project.sodam365.entity.User;
import com.project.sodam365.service.NuserService;
import com.project.sodam365.service.UserService;
import com.project.sodam365.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://192.168.0.66:3000", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}, allowCredentials = "true")
public class UserController {
    private final UserService userService;
    private final NuserService nuserService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, NuserService nuserService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.nuserService = nuserService;
        this.jwtUtil = jwtUtil;
    }

    // 테스트 엔드포인트
    @GetMapping("/test")
    public ResponseEntity<List<String>> testMVC() {
        return ResponseEntity.ok(List.of("리액트", "스프링"));
    }

    // 모든 사용자 조회
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getUsers() {
        List<User> userList = userService.getAllUsers();

        if (ObjectUtils.isEmpty(userList)) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        List<UserDto> userDtoList = userList.stream()
                .map(UserDto::fromUserEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(userDtoList);
    }

    // 사용자 생성
    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody UserDto userDto) {
        userService.createUser(userDto);
        return ResponseEntity.ok("User created successfully");
    }

    // 사용자 생성
    @PostMapping("/nusers")
    public ResponseEntity<String> createNuser(@RequestBody NuserDto nuserDto) {
        nuserService.createUser(nuserDto);
        return ResponseEntity.ok("User created successfully");
    }


    @GetMapping("/users/check-duplicate")
    public ResponseEntity<Map<String, Object>> confirmNID(@RequestParam(name = "n_userid", required = false) String n_userid,
                                                          @RequestParam(name = "nUserid", required = false) String nUserid) {
        String userId = (n_userid != null) ? n_userid : nUserid; // 둘 중 하나 받기

        Map<String, Object> response = new HashMap<>();

        // 요청된 아이디가 없을 경우 400 응답
        if (userId == null || userId.isEmpty()) {
            response.put("isDuplicate", false);
            response.put("message", "아이디를 입력해주세요.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        boolean isDuplicate = userService.isUserIdDuplicate(userId) || nuserService.isNUserIdDuplicate(userId);

        response.put("isDuplicate", isDuplicate);

        if (isDuplicate) {
            response.put("message", "이미 사용 중인 아이디입니다.");
        } else {
            response.put("message", "사용 가능한 아이디입니다.");
        }

        return ResponseEntity.ok(response);
    }



    // 비즈니스 사용자 아이디 중복 확인 (메서드 이름 변경 ✅)
    @GetMapping("/users/check-duplicate2")
    public ResponseEntity<Map<String, Object>> confirmBusinessID(@RequestParam("userid") String userid) {
        boolean isDuplicate = userService.isUserIdDuplicate(userid) || nuserService.isNUserIdDuplicate(userid);


        Map<String, Object> response = new HashMap<>();
        response.put("isDuplicate", isDuplicate);

        if (isDuplicate) {
            response.put("message", "이미 사용 중인 아이디입니다.");
        } else {
            response.put("message", "사용 가능한 아이디입니다.");
        }

        return ResponseEntity.ok(response);
    }
    // 🔹 사업자 사용자 정보 조회
    @GetMapping("/users/business/info")
    public ResponseEntity<UserDto> getBusinessUserInfo(@RequestHeader("Authorization") String token) {
        String userId = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(userService.getUserInfo(userId));
    }

    // 🔹 일반 사용자 정보 조회
    @GetMapping("/users/normal/info")
    public ResponseEntity<NuserDto> getNormalUserInfo(@RequestHeader("Authorization") String token) {
        String userId = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(nuserService.getUserInfo(userId));
    }

    // 🔹 사업자 정보 수정
    @PutMapping("/users/business/update")
    public ResponseEntity<String> updateBusinessUser(@RequestHeader("Authorization") String token,
                                                     @RequestBody Map<String, Object> userInfo) {
        String userId = jwtUtil.extractUserId(token);
        userService.updateUser(userId, userInfo);
        return ResponseEntity.ok("사업자 정보 수정 완료");
    }

    // 🔹 일반 사용자 정보 수정
    @PutMapping("/users/normal/update")
    public ResponseEntity<String> updateNormalUser(@RequestHeader("Authorization") String token,
                                                   @RequestBody Map<String, Object> userInfo) {
        String userId = jwtUtil.extractUserId(token);
        nuserService.updateUser(userId, userInfo);
        return ResponseEntity.ok("일반 사용자 정보 수정 완료");
    }

    // 🔹 사업자 탈퇴
    @DeleteMapping("/users/business/delete")
    public ResponseEntity<String> deleteBusinessUser(@RequestHeader("Authorization") String token) {
        String userId = jwtUtil.extractUserId(token);
        userService.deleteUser(userId);
        return ResponseEntity.ok("사업자 계정이 삭제되었습니다.");
    }

    // 🔹 일반 사용자 탈퇴
    @DeleteMapping("/users/normal/delete")
    public ResponseEntity<String> deleteNormalUser(@RequestHeader("Authorization") String token) {
        String userId = jwtUtil.extractUserId(token);
        nuserService.deleteUser(userId);
        return ResponseEntity.ok("일반 사용자 계정이 삭제되었습니다.");
    }

    // 🔹 사업자 비밀번호 변경
    @PutMapping("/users/business/password")
    public ResponseEntity<String> changeBusinessPassword(@RequestHeader("Authorization") String token,
                                                         @RequestBody Map<String, String> pwMap) {
        String userid = jwtUtil.extractUserId(token);
        userService.changePassword(userid, pwMap.get("currentPassword"), pwMap.get("newPassword"));
        return ResponseEntity.ok("비밀번호가 변경되었습니다.");
    }

    // 🔹 일반 사용자 비밀번호 변경
    @PutMapping("/users/normal/password")
    public ResponseEntity<String> changeNormalPassword(@RequestHeader("Authorization") String token,
                                                       @RequestBody Map<String, String> pwMap) {
        String nUserid = jwtUtil.extractUserId(token);
        nuserService.changePassword(nUserid, pwMap.get("currentPassword"), pwMap.get("newPassword"));
        return ResponseEntity.ok("비밀번호가 변경되었습니다.");

    }


}
