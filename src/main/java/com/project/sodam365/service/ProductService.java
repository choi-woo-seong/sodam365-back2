package com.project.sodam365.service;

import com.project.sodam365.dto.ProductDto;
import com.project.sodam365.entity.Product;
import com.project.sodam365.entity.User;
import com.project.sodam365.repository.ProductRepository;
import com.project.sodam365.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // 상품 등록 (비즈니스 사용자만 가능)
    public ProductDto createProduct(ProductDto productDto) {
        User user = userRepository.findByUserid(productDto.getUserid())
                .orElseThrow(() -> new IllegalArgumentException("등록할 비즈니스 사용자가 존재하지 않습니다."));

        Product product = Product.builder()
                .p_title(productDto.getP_title())
                .p_contents(productDto.getP_contents())
                .p_price(productDto.getP_price())
                .p_link(productDto.getP_link())
                .user(user)
                .build();

        Product savedProduct = productRepository.save(product);
        return new ProductDto(
                savedProduct.getNo(),
                savedProduct.getP_title(),
                savedProduct.getP_contents(),
                savedProduct.getP_price(),
                savedProduct.getP_link(),
                savedProduct.getCreatedAt(), // ✅ 순서 수정
                savedProduct.getUser().getName(), // ✅ username 필드 올바르게 매핑
                savedProduct.getUser().getUserid() // ✅ userid 필드 올바르게 매핑
        );
    }

    // ✅ 특정 상품 조회 (no 값 기반)
    public ProductDto getProductByNo(Long no) {
        Product product = productRepository.findByNo(no)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다: " + no));

        return ProductDto.fromEntity(product);
    }

    // 상품 목록 조회
    public List<ProductDto> getAllProducts() {
        return productRepository.findAllProductsWithUsername().stream()
                .map(product -> new ProductDto(product.getNo(), product.getP_title(), product.getP_contents(), product.getP_price(), product.getP_link(), product.getCreatedAt(), product.getUsername(), product.getUserid()))
                .collect(Collectors.toList());
    }

    // 특정 사용자(User)의 상품 조회
    public List<ProductDto> getProductsByUser(String userid) {
        return productRepository.findByUser_Userid(userid).stream()
                .map(product -> new ProductDto(product.getNo(), product.getP_title(), product.getP_contents(), product.getP_price(), product.getP_link(), product.getCreatedAt(), product.getUser().getName(), product.getUser().getUserid()))
                .collect(Collectors.toList());
    }

    // 상품 수정 (해당 상품의 등록자만 가능)
    @Transactional
    public ProductDto updateProduct(Long no, ProductDto productDto, String userid) {
        // ✅ 상품 존재 여부 확인
        Product product = productRepository.findByNo(no)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다: " + no));

        // ✅ JWT에서 추출한 userid와 상품 등록자의 userid 비교 (권한 체크)
        if (!product.getUser().getUserid().equals(userid)) {
            throw new AccessDeniedException("해당 상품을 수정할 권한이 없습니다.");
        }

        // ✅ 상품 정보 업데이트
        product.setP_title(productDto.getP_title());
        product.setP_contents(productDto.getP_contents());
        product.setP_price(productDto.getP_price());
        product.setP_link(productDto.getP_link());

        // ✅ DB 저장 후 DTO 변환하여 반환
        Product updatedProduct = productRepository.save(product);

        return ProductDto.fromEntity(updatedProduct);
    }

    // 상품 삭제
    @Transactional
    public void deleteProduct(Long no, String userid) {
        // ✅ 상품 존재 여부 확인
        Product product = productRepository.findByNo(no)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다: " + no));

        // ✅ JWT에서 추출한 userid와 상품 등록자의 userid 비교 (권한 체크)
        if (!product.getUser().getUserid().equals(userid)) {
            throw new AccessDeniedException("해당 상품을 삭제할 권한이 없습니다.");
        }

        // ✅ 상품 삭제 (하드 삭제)
        productRepository.delete(product);
    }

}
