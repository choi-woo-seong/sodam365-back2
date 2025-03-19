package com.project.sodam365.repository;

import com.project.sodam365.entity.Recent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecentRepository extends JpaRepository<Recent, Long> {
    List<Recent> findTop3ByUseridOrderByViewedAtDesc(String userid);
    void deleteByUseridAndPostNo(String userid, Long postNo);
}
