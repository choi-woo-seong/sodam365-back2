package com.project.sodam365.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyPostsResponseDto {

    private List<ProductDto> products;
    private List<BizDto> bizList;
    private List<CommunityDto> communities;
    private List<QuestionDto> questions;

    private String userType; // ← 🔥 추가! "buser" 또는 "nuser"

}
