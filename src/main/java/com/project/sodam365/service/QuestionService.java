package com.project.sodam365.service;

import com.project.sodam365.dto.QuestionDto;
import com.project.sodam365.entity.Question;
import com.project.sodam365.repository.NuserRepository;
import com.project.sodam365.repository.QuestionRepository;
import com.project.sodam365.repository.UserRepository;
import com.project.sodam365.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final NuserRepository nuserRepository;
    private final JwtUtil jwtUtil;

    public QuestionDto createQuestion(QuestionDto dto, String token) {
        String userId = jwtUtil.extractUsername(token);
        String name = jwtUtil.extractName(token); // ✅ 이름도 추출

        Question question = new Question();
        question.setTitle(dto.getTitle());
        question.setContent(dto.getContent());
        question.setWriter(userId);
        question.setWriterName(name); // ✅ 여기!

        if (userRepository.findById(userId).isPresent()) {
            question.setUser(userRepository.findById(userId).get());
        } else if (nuserRepository.findById(userId).isPresent()) {
            question.setNuser(nuserRepository.findById(userId).get());
        } else {
            throw new RuntimeException("로그인 유저를 찾을 수 없습니다.");
        }

        return QuestionDto.fromEntity(questionRepository.save(question));
    }


    public void deleteQuestion(Long id, String token) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("질문이 존재하지 않음"));

        String userId = jwtUtil.extractUsername(token);
        boolean isAdmin = jwtUtil.isAdmin(token);

        if ((question.getUser() != null && question.getUser().getUserid().equals(userId)) ||
                (question.getNuser() != null && question.getNuser().getNUserid().equals(userId)) ||
                isAdmin) {
            questionRepository.delete(question);
        } else {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }
    }

    public List<QuestionDto> getAllQuestions() {
        return questionRepository.findAll().stream()
                .map(QuestionDto::fromEntity)
                .toList();
    }



    public QuestionDto getQuestion(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("질문 없음"));
        return QuestionDto.fromEntity(question);
    }


    public QuestionDto updateQuestion(Long id, QuestionDto dto, String token) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("질문 없음"));

        String userId = jwtUtil.extractUsername(token);
        boolean isAdmin = jwtUtil.isAdmin(token);

        boolean isOwner = (question.getUser() != null && question.getUser().getUserid().equals(userId)) ||
                (question.getNuser() != null && question.getNuser().getNUserid().equals(userId));

        if (!isOwner && !isAdmin) {
            throw new RuntimeException("수정 권한 없음");
        }

        question.setTitle(dto.getTitle());
        question.setContent(dto.getContent());

        return QuestionDto.fromEntity(questionRepository.save(question));
    }
}
