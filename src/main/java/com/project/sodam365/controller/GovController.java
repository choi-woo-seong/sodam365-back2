package com.project.sodam365.controller;

import com.project.sodam365.dto.GovDto;
import com.project.sodam365.entity.Gov;
import com.project.sodam365.service.GovService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gov")
public class GovController {

    private final GovService govService;

    @GetMapping("/fetch")
    public ResponseEntity<String> fetchGovData() {
        govService.fetchAndSaveGovData();
        return ResponseEntity.ok("정부 대출 데이터 저장 완료");
    }

    @GetMapping("/searchAll")
    public ResponseEntity<List<GovDto>> getGovList() {
        List<Gov> govList = govService.getAllGovData();
        List<GovDto> dtoList = govList.stream()
                .map(GovDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/govDetail/{id}")
    public ResponseEntity<GovDto> getGovDetail(@PathVariable Long id) {
        Gov gov = govService.getGovById(id);
        return ResponseEntity.ok(GovDto.fromEntity(gov));
    }
}
