package com.project.sodam365.controller;

import com.project.sodam365.dto.SearchResultDto;
import com.project.sodam365.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<SearchResultDto>> searchAll(@RequestParam String title) {
        if (title == null || title.trim().isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList()); // []
        }
        return ResponseEntity.ok(searchService.searchAll(title));
    }

}
