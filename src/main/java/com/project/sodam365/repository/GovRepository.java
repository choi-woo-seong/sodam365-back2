package com.project.sodam365.repository;

import com.project.sodam365.entity.Gov;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GovRepository extends JpaRepository<Gov, Long> {
    List<Gov> findTop3ByOrderByNoDesc();
    @Query("SELECT g FROM Gov g WHERE g.finPrdNm LIKE %:title%")
    List<Gov> findByTitleContaining(@Param("title") String title);



}
