package com.project.sodam365.dto;

import com.project.sodam365.entity.Gov;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GovDto {
    private Long no;
    private String finPrdNm;
    private String irt;
    private String lnLmt;
    private String trgt;
    private String hdlInst;
    private String maxTotLnTrm;
    private String rdptMthd;
    private String g_title;
    private String usge;
    private LocalDateTime createdAt;

    @Builder
    public GovDto(Long no, String finPrdNm, String irt, String lnLmt, String trgt,
                  String hdlInst, String maxTotLnTrm, String rdptMthd,
                  String g_title, String usge, LocalDateTime createdAt) {
        this.no = no;
        this.finPrdNm = finPrdNm;
        this.irt = irt;
        this.lnLmt = lnLmt;
        this.trgt = trgt;
        this.hdlInst = hdlInst;
        this.maxTotLnTrm = maxTotLnTrm;
        this.rdptMthd = rdptMthd;
        this.g_title = g_title;
        this.usge = usge;
        this.createdAt=createdAt;
    }

    public static GovDto fromEntity(Gov gov) {
        return GovDto.builder()
                .no(gov.getNo())
                .finPrdNm(gov.getFinPrdNm())
                .irt(gov.getIrt())
                .lnLmt(gov.getLnLmt())
                .trgt(gov.getTrgt())
                .hdlInst(gov.getHdlInst())
                .maxTotLnTrm(gov.getMaxTotLnTrm())
                .rdptMthd(gov.getRdptMthd())
                .g_title(gov.getG_title())
                .usge(gov.getUsge())
                .createdAt(gov.getCreatedAt())
                .build();
    }
}