package com.project.sodam365.repository;

import com.project.sodam365.entity.Answer;
import com.project.sodam365.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findTop3ByOrderByIdDesc();
    // 사업자 사용자가 작성한 질문 조회
    List<Question> findByUser_Userid(String userid);

    // 일반 사용자가 작성한 질문 조회
    List<Question> findByNuser_nUserid(String userid);


}
