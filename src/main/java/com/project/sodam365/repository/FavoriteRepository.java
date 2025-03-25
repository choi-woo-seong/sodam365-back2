package com.project.sodam365.repository;

import com.project.sodam365.entity.Favorite;
import com.project.sodam365.entity.FavoriteType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import com.project.sodam365.entity.User;
import com.project.sodam365.entity.Nuser;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    // user, nuser 기준으로 직접 조회
    List<Favorite> findByUser(User user);
    List<Favorite> findByNuser(Nuser nuser);

    Optional<Favorite> findByUserAndTargetIdAndTargetType(User user, Long targetId, FavoriteType targetType);
    Optional<Favorite> findByNuserAndTargetIdAndTargetType(Nuser nuser, Long targetId, FavoriteType targetType);
}

