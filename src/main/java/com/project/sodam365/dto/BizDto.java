package com.project.sodam365.dto;

import com.project.sodam365.entity.Biz;
import com.project.sodam365.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BizDto {
    private Long no;
    private String b_title;
    private String b_contents;
    private String b_link;
    private String b_price;
    private String userid;
    private String username;
    private LocalDateTime createdDate;

    public static BizDto fromEntity(Biz biz) {
        return BizDto.builder()
                .no(biz.getNo())
                .b_title(biz.getB_title())
                .b_contents(biz.getB_contents())
                .b_link(biz.getB_link())
                .b_price(biz.getB_price())
                .userid(biz.getUserid().getUserid()) // ✅ User 엔티티에서 userid 가져오기
                .createdDate(biz.getCreatedAt()) // BaseTimeEntity 상속된 createdAt 사용
                .username(biz.getUserid().getName()) // User 엔티티에서 가져옴
                .build();
    }

    public static Biz toEntity(BizDto bizDto, User user) { // ✅ User 객체 포함
        return Biz.builder()
                .no(bizDto.getNo())
                .b_title(bizDto.getB_title())
                .b_contents(bizDto.getB_contents())
                .b_link(bizDto.getB_link())
                .b_price(bizDto.getB_price())
                .userid(user) // ✅ User 엔티티 포함
                .build();
    }
}
