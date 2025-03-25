package com.project.sodam365.service;

import com.project.sodam365.dto.AnswerDto;
import com.project.sodam365.entity.Answer;
import com.project.sodam365.entity.Question;
import com.project.sodam365.repository.AnswerRepository;
import com.project.sodam365.repository.QuestionRepository;
import com.project.sodam365.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        answer.setA_title(dto.getA_title());
        answer.setA_contents(dto.getA_contents());
        answer.setAnswer(dto.getAnswer());
        answer.setAdminId(jwtUtil.extractUsername(token));
        answer.setQuestion(question);

        // 🔥 양방향 연관관계 설정 명시적으로 추가
        question.setAnswer(answer);
        question.setAnswered(true);

        // 🔄 반드시 answer 먼저 저장
        answerRepository.save(answer);
        questionRepository.save(question);

        return answer;
    }


    public List<Answer> getAllAnswers() {
        return answerRepository.findAll();
    }

    public Answer getAnswerById(Long id) {
        return answerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("답변 없음"));
    }

    public AnswerDto getAnswerByQuestionId(Long questionId) {
        Answer answer = answerRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new RuntimeException("해당 질문에 대한 답변이 없습니다."));
        return AnswerDto.fromEntity(answer);
    }

    @Transactional
    public Answer updateAnswer(Long id, AnswerDto dto, String token) {
        if (!jwtUtil.isAdmin(token)) {
            throw new IllegalArgumentException("관리자만 수정 가능");
        }

        Answer answer = getAnswerById(id);
        answer.setA_title(dto.getA_title());
        answer.setA_contents(dto.getA_contents());
        answer.setAnswer(dto.getAnswer());

        return answerRepository.save(answer);
    }

    @Transactional
    public void deleteAnswer(Long id, String token) {
        if (!jwtUtil.isAdmin(token)) {
            throw new IllegalArgumentException("관리자만 삭제 가능");
        }

        Answer answer = getAnswerById(id);
        Question question = answer.getQuestion();

        // 연관관계 끊기
        question.setAnswer(null);
        question.setAnswered(false);
        questionRepository.save(question);

        answerRepository.delete(answer);
    }
}

