package com.project.sodam365.controller;

import com.project.sodam365.dto.ProductDto;
import com.project.sodam365.service.ProductService;
import com.project.sodam365.service.UserService;
import com.project.sodam365.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final JwtUtil jwtUtil;
    private final UserService userService;


    // 상품 등록 (비즈니스 사용자만 가능, JWT 인증 필요)
    @PostMapping("/create")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto,
                                                    @RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", ""); // "Bearer " 제거
        String userid = jwtUtil.extractUsername(jwt); // JWT에서 userid 추출

        productDto.setUserid(userid); // JWT에서 추출한 userid 설정
        return ResponseEntity.ok(productService.createProduct(productDto));
    }

    // 전체 상품 조회
    @GetMapping("/searchAll")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // ✅ 특정 상품 조회 (no 값 기반)
    @GetMapping("/productDetail/{no}")
    public ResponseEntity<ProductDto> getProductByNo(@PathVariable Long no) {
        return ResponseEntity.ok(productService.getProductByNo(no));
    }

    // ✅ 상품 수정 (JWT 인증 적용, 등록한 사용자만 가능)
    @PutMapping("/productUpdate/{no}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long no,
            @RequestBody ProductDto productDto,
            @RequestHeader("Authorization") String token) { // 🔥 JWT 토큰에서 사용자 정보 추출

        // ✅ `JwtUtil`이 Bearer 자동 제거하므로, 추가 처리 없이 그대로 사용
        String userid = jwtUtil.extractUsername(token);

        return ResponseEntity.ok(productService.updateProduct(no, productDto, userid));
    }

    // ✅ 상품 삭제 (JWT 인증 적용, 등록한 사용자만 가능)
    @DeleteMapping("/productDelete/{no}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long no,
            @RequestHeader("Authorization") String token) { // 🔥 JWT 토큰에서 사용자 정보 추출

        // ✅ JWT에서 userid 추출
        String userid = jwtUtil.extractUsername(token);

        // ✅ 서비스 호출 (상품 삭제)
        productService.deleteProduct(no, userid);

        return ResponseEntity.noContent().build(); // ✅ 성공 시 204 No Content 반환
    }
}
