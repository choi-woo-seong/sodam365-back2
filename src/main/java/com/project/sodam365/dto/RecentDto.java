package com.project.sodam365.dto;

import com.project.sodam365.entity.Recent;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecentDto {
    private Long postNo;
    private String category;
    private String title;
    private LocalDateTime viewedAt;

    public static RecentDto fromEntity(Recent recent) {
        return RecentDto.builder()
                .postNo(recent.getPostNo())
                .category(recent.getCategory())
                .title(recent.getTitle())
                .viewedAt(recent.getViewedAt())
                .build();
    }
}
