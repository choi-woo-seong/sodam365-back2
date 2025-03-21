package com.project.sodam365.service;

import com.project.sodam365.dto.AnswerDto;
import com.project.sodam365.entity.Answer;
import com.project.sodam365.entity.Question;
import com.project.sodam365.repository.AnswerRepository;
import com.project.sodam365.repository.QuestionRepository;
import com.project.sodam365.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final JwtUtil jwtUtil;

    public Answer createAnswer(AnswerDto dto, String token) {
        if (!jwtUtil.isAdmin(token)) {
            throw new IllegalArgumentException("관리자만 답변할 수 있습니다.");
        }

        Question question = questionRepository.findById(dto.getQuestionId())
                .orElseThrow(() -> new RuntimeException("질문 없음"));

        if (question.isAnswered()) {
            throw new RuntimeException("이미 답변된 질문입니다.");
        }

        Answer answer = new Answer();
        answer.setContent(dto.getContent());
        answer.setAdminId(jwtUtil.extractUsername(token));
        answer.setQuestion(question);

        question.setAnswered(true);
        questionRepository.save(question); // 상태 업데이트

        return answerRepository.save(answer);
    }

    public List<Answer> getAllAnswers() {
        return answerRepository.findAll();
    }


    public Answer getAnswerById(Long id) {
        return answerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("답변 없음"));
    }


    public Answer updateAnswer(Long id, AnswerDto dto, String token) {
        if (!jwtUtil.isAdmin(token)) {
            throw new IllegalArgumentException("관리자만 수정 가능");
        }

        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("답변 없음"));

        answer.setContent(dto.getContent());
        return answerRepository.save(answer);
    }


    public void deleteAnswer(Long id, String token) {
        if (!jwtUtil.isAdmin(token)) {
            throw new IllegalArgumentException("관리자만 삭제 가능");
        }

        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("답변 없음"));

        // 연결된 질문 상태 초기화
        Question question = answer.getQuestion();
        question.setAnswered(false);
        questionRepository.save(question);

        answerRepository.delete(answer);
    }

}
