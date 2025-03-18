package com.project.sodam365.controller;

import com.project.sodam365.dto.ProductDto;
import com.project.sodam365.service.ProductService;
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
    @GetMapping("/{no}")
    public ResponseEntity<ProductDto> getProductByNo(@PathVariable Long no) {
        return ResponseEntity.ok(productService.getProductByNo(no));
    }

    // 상품 수정 (등록한 사용자만 가능)
    @PutMapping("/{no}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long no, @RequestBody ProductDto productDto, @RequestParam String userid) {
        return ResponseEntity.ok(productService.updateProduct(no, productDto, userid));
    }

    // 상품 삭제 (등록한 사용자만 가능, 하드 삭제)
    @DeleteMapping("/{no}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long no, @RequestParam String userid) {
        productService.deleteProduct(no, userid);
        return ResponseEntity.noContent().build();
    }
}
