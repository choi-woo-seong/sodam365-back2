package com.project.sodam365.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass  // 공통 매핑 정보
@EntityListeners(AuditingEntityListener.class)  // Auditing 기능 활성화
public abstract class BaseTimeEntity {

    @CreatedDate  // 최초 저장될 때 자동 시간 설정
    @Column(updatable = false)  // 수정 불가
    private LocalDateTime createdAt;

    @LastModifiedDate  // 수정될 때 자동 시간 갱신
    private LocalDateTime updatedAt;

    // ✅ 명시적 getter 추가!
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
