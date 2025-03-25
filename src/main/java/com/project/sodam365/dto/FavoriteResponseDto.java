package com.project.sodam365.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.sodam365.entity.Favorite;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FavoriteResponseDto {
    private Long id;
    private String title;
    private String type;
    private Long targetId;
    private String targetPgm;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    // 💥 Jackson이 인식할 수 있도록 명시적 getter 추가
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public static FavoriteResponseDto from(Favorite fav, String title) {
        FavoriteResponseDto dto = new FavoriteResponseDto();
        dto.setId(fav.getId());
        dto.setTargetId(fav.getTargetId());
        dto.setType(fav.getTargetType().name());
        dto.setTargetPgm(fav.getTargetPgm());
        dto.setCreatedAt(fav.getCreatedAt());
        dto.setTitle(title);
        return dto;
    }
}
