package com.project.sodam365.repository;

import com.project.sodam365.entity.Biz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BizRepository extends JpaRepository<Biz, Long> {
    List<Biz> findTop3ByOrderByNoDesc();
    // 정확한 객체명과 필드명 사용
    List<Biz> findByUserid_Userid(String userid);
    @Query("SELECT b FROM Biz b WHERE b.b_title LIKE %:title%")
    List<Biz> findByTitleContaining(@Param("title") String title);


}
