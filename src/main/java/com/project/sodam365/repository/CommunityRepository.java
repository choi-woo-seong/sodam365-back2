package com.project.sodam365.repository;

import com.project.sodam365.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {
    List<Community> findTop3ByOrderByIdDesc(); // ✅ 'id' 필드 기준 정렬
    // ✅ 연관관계: Community → User (user)
    List<Community> findByUser_Userid(String userid);

    // ✅ 연관관계: Community → Nuser (nuser)
    List<Community> findByNuser_nUserid(String userid);

    @Query("SELECT c FROM Community c WHERE c.c_title LIKE %:title%")
    List<Community> findByTitleContaining(@Param("title") String title);



}
