package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.QuestionDTO;

import java.util.List;
import java.util.Optional;

public interface QuestionService {
    List<QuestionDTO> getAllQuestions();

    Optional<QuestionDTO> getQuestionById(Long id);

    QuestionDTO saveQuestion(QuestionDTO questionDTO, Long idOffer);

    QuestionDTO updateQuestion(Long id, QuestionDTO questionDTO);

    void deleteQuestion(Long id);
}
