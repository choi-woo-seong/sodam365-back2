// FavoriteToggleRequest.java
package com.project.sodam365.dto;

import com.project.sodam365.entity.FavoriteType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoriteToggleRequest {
    private Long targetId;
    private FavoriteType targetType;
}
