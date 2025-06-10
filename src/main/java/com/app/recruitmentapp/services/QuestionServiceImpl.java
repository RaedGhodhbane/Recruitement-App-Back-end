package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Offer;
import com.app.recruitmentapp.entities.Question;
import com.app.recruitmentapp.repositories.OfferRepository;
import com.app.recruitmentapp.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private OfferRepository offerRepository;

    @Override
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @Override
    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    @Override
    public Question saveQuestion(Question question, Long idOffer) {
        Offer o = offerRepository.findById(idOffer).orElse(null);
        question.setOffer(o);
        return questionRepository.save(question);
    }

    @Override
    public Question updateQuestion(Long id, Question newQuestion) {
        Question q = questionRepository.findById(id).orElse(null);
        q.setTitle(newQuestion.getTitle());
        q.setChoice1(newQuestion.getChoice1());
        q.setChoice2(newQuestion.getChoice2());
        q.setChoice3(newQuestion.getChoice3());
        q.setResponse(newQuestion.getResponse());
        q.setOffer(newQuestion.getOffer());
        questionRepository.saveAndFlush(q);
        return q;
    }

    @Override
    public void deleteQuestion(Long id) {
        if (questionRepository.existsById(id)) {
            questionRepository.deleteById(id);
        } else {
            throw new RuntimeException("Candidat non trouvé");
        }
    }

}
