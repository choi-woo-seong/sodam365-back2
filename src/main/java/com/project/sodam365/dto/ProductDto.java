package com.project.sodam365.dto;

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
    private String userid; // 상품을 등록한 비즈니스 사용자 ID
}
