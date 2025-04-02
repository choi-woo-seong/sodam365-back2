package com.project.sodam365.dto;

import com.project.sodam365.entity.Product;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long no;
    private String p_title;
    private String p_contents;
    private String p_price;
    private String p_link;
    private LocalDateTime createdAt;
    private String username; // 상품을 등록한 사용자의 이름
    private String userid;   // 상품을 등록한 비즈니스 사용자 ID
    private String ownerloc; // 🧭 길찾기용 유저 주소


    // ✅ Entity → DTO 변환 메서드
    public static ProductDto fromEntity(Product product) {
        return ProductDto.builder()
                .no(product.getNo())
                .p_title(product.getP_title())
                .p_contents(product.getP_contents())
                .p_price(product.getP_price())
                .p_link(product.getP_link())
                .createdAt(product.getCreatedAt()) // BaseTimeEntity 상속된 createdAt 사용
                .username(product.getUser().getName()) // User 엔티티에서 가져옴
                .userid(product.getUser().getUserid()) // User 엔티티에서 가져옴
                .ownerloc(product.getUser().getOwnerloc())
                .build();
    }
}
