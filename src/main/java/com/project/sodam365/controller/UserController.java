package com.project.sodam365.controller;

import com.project.sodam365.dto.NuserDto;
import com.project.sodam365.dto.UserDto;
import com.project.sodam365.entity.User;
import com.project.sodam365.service.NuserService;
import com.project.sodam365.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://192.168.0.66:3000", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}, allowCredentials = "true")
public class UserController {
    private final UserService userService;
    private final NuserService nuserService;

    public UserController(UserService userService, NuserService nuserService) {
        this.userService = userService;
        this.nuserService = nuserService;
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


    // 아이디 중복 확인
    @GetMapping("/users/check-duplicate")
    public ResponseEntity<Boolean> confirmNID(@RequestParam("n_userid") String n_userid) {
        boolean isDuplicate = nuserService.existsById(n_userid);
        return ResponseEntity.ok(isDuplicate);
    }

    @GetMapping("/users/check-duplicate2")
    public ResponseEntity<Boolean> confirmID(@RequestParam("userid") String userid) {
        boolean isDuplicate = userService.existsById(userid);
        return ResponseEntity.ok(isDuplicate);
    }

}
