package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Question;
import com.app.recruitmentapp.entities.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionService {
    List<Question> getAllQuestions();

    Optional<Question> getQuestionById(Long id);

    Question saveQuestion(Question question, Long idOffer);

    Question updateQuestion(Long id, Question question);

    void deleteQuestion(Long id);
}
