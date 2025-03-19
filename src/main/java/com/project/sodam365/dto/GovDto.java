package com.project.sodam365.dto;

import com.project.sodam365.entity.Gov;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GovDto {
    private Long no;
    private String g_title;
    private String financial_product;
    private String irt;
    private String lnLmt;

    public static GovDto fromEntity(Gov gov) {
        return GovDto.builder()
                .no(gov.getNo())
                .g_title(gov.getG_title())
                .financial_product(gov.getFinancial_product())
                .irt(gov.getIrt())
                .lnLmt(gov.getLnLmt())
                .build();
    }
}
