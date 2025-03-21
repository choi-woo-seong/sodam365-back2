package com.project.sodam365.repository;

import com.project.sodam365.entity.Answer;
import com.project.sodam365.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findTop3ByOrderByIdDesc();

}
