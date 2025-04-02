package com.project.sodam365.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultDto {
    private String targetPgm;  // "productDetail", "govDetail", etc.
    private Long id;
    private String title;
    private String summary;

}
