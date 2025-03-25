package com.project.sodam365.service;

import com.project.sodam365.dto.FavoriteResponseDto;
import com.project.sodam365.entity.*;
import com.project.sodam365.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;
    private final GovRepository govRepository;
    private final BizRepository bizRepository;
    private final UserRepository userRepository;
    private final NuserRepository nuserRepository;

    @Transactional
    public boolean toggleFavorite(User user, Nuser nuser, Long targetId, FavoriteType targetType) {
        Optional<Favorite> favoriteOpt;

        String pgm = switch (targetType) {
            case PRODUCT -> "productDetail";
            case GOV     -> "bankDetail";
            case BIZ     -> "businessDetail";
        };

        if (user != null) {
            favoriteOpt = favoriteRepository.findByUserAndTargetIdAndTargetType(user, targetId, targetType);
        } else {
            favoriteOpt = favoriteRepository.findByNuserAndTargetIdAndTargetType(nuser, targetId, targetType);
        }

        if (favoriteOpt.isPresent()) {
            favoriteRepository.delete(favoriteOpt.get());
            return false;
        }

        Favorite fav = new Favorite();
        fav.setTargetId(targetId);
        fav.setTargetType(targetType);
        fav.setUser(user);
        fav.setNuser(nuser);
        fav.setTargetPgm(pgm);
        favoriteRepository.save(fav);

        return true;
    }

    public List<Favorite> getFavoritesByUser(String userId) {
        User user = userRepository.findByUserid(userId)
                .orElseThrow(() -> new RuntimeException("해당 user 없음"));
        return favoriteRepository.findByUser(user);
    }

    public List<Favorite> getFavoritesByNuser(String nuserId) {
        Nuser nuser = nuserRepository.findByNUserid(nuserId)
                .orElseThrow(() -> new RuntimeException("해당 nuser 없음"));
        return favoriteRepository.findByNuser(nuser);
    }

    public boolean isFavoritedByUser(String userId, Long targetId, FavoriteType type) {
        User user = userRepository.findByUserid(userId)
                .orElseThrow(() -> new RuntimeException("해당 user 없음"));
        return favoriteRepository.findByUserAndTargetIdAndTargetType(user, targetId, type).isPresent();
    }

    public boolean isFavoritedByNuser(String userId, Long targetId, FavoriteType type) {
        Nuser nuser = nuserRepository.findByNUserid(userId)
                .orElseThrow(() -> new RuntimeException("해당 nuser 없음"));
        return favoriteRepository.findByNuserAndTargetIdAndTargetType(nuser, targetId, type).isPresent();
    }

    public List<FavoriteResponseDto> getMappedFavorites(String userType, String userId) {
        List<Favorite> favorites;

        if ("buser".equals(userType)) {
            User user = userRepository.findByUserid(userId)
                    .orElseThrow(() -> new RuntimeException("유저 없음"));
            favorites = favoriteRepository.findByUser(user);
        } else {
            Nuser nuser = nuserRepository.findByNUserid(userId)
                    .orElseThrow(() -> new RuntimeException("일반 유저 없음"));
            favorites = favoriteRepository.findByNuser(nuser);
        }

        List<FavoriteResponseDto> result = new ArrayList<>();

        for (Favorite fav : favorites) {
            System.out.println("fav.getCreatedAt() = " + fav.getCreatedAt()); // ✅ 여기!

            FavoriteResponseDto dto = new FavoriteResponseDto();
            dto.setId(fav.getId());
            dto.setTargetId(fav.getTargetId());
            dto.setType(fav.getTargetType().name());
            dto.setTargetPgm(fav.getTargetPgm());
            dto.setCreatedAt(fav.getCreatedAt());

            switch (fav.getTargetType()) {
                case PRODUCT -> {
                    productRepository.findById(fav.getTargetId())
                            .ifPresent(product -> dto.setTitle(product.getP_title()));
                }
                case GOV -> {
                    govRepository.findById(fav.getTargetId())
                            .ifPresent(gov -> dto.setTitle(gov.getFinPrdNm()));
                }
                case BIZ -> {
                    bizRepository.findById(fav.getTargetId())
                            .ifPresent(biz -> dto.setTitle(biz.getB_title()));
                }
            }

            result.add(dto);
        }

        return result;
    }

}

