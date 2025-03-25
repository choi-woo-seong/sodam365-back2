package com.project.sodam365.repository;

import com.project.sodam365.entity.Biz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BizRepository extends JpaRepository<Biz, Long> {
    List<Biz> findTop3ByOrderByNoDesc();
    // 정확한 객체명과 필드명 사용
    List<Biz> findByUserid_Userid(String userid);


}
