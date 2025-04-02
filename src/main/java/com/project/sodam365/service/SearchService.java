package com.project.sodam365.service;

import com.project.sodam365.dto.SearchResultDto;
import com.project.sodam365.repository.BizRepository;
import com.project.sodam365.repository.CommunityRepository;
import com.project.sodam365.repository.GovRepository;
import com.project.sodam365.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class SearchService {
    private final ProductRepository productRepository;
    private final BizRepository bizRepository;
    private final CommunityRepository communityRepository;
    private final GovRepository govRepository;

    public List<SearchResultDto> searchAll(String title) {
        List<SearchResultDto> results = new ArrayList<>();

        productRepository.findByTitleContaining(title).forEach(p ->
                results.add(new SearchResultDto("productDetail", p.getNo(), p.getP_title(), p.getP_contents()))
        );

        bizRepository.findByTitleContaining(title).forEach(b ->
                results.add(new SearchResultDto("businessDetail", b.getNo(), b.getB_title(), b.getB_contents()))
        );

        communityRepository.findByTitleContaining(title).forEach(c ->
                results.add(new SearchResultDto("communityDetail", c.getId(), c.getC_title(), c.getC_content()))
        );

        govRepository.findByTitleContaining(title).forEach(g ->
                results.add(new SearchResultDto(
                        "bankDetail",
                        g.getNo(),
                        g.getFinPrdNm(),  // ✅ 이제 이게 진짜 제목!
                        g.getTrgt()       // 요약용 내용
                ))
        );


        return results;
    }

}
