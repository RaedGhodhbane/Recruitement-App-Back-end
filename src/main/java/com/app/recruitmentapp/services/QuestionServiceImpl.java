package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.QuestionDTO;
import com.app.recruitmentapp.entities.Offer;
import com.app.recruitmentapp.entities.Question;
import com.app.recruitmentapp.mapper.EntityMapper;
import com.app.recruitmentapp.exceptions.ResourceNotFoundException;
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
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public List<QuestionDTO> getAllQuestions() {
        return entityMapper.toQuestionDTOList(questionRepository.findAll());
    }

    @Override
    public Optional<QuestionDTO> getQuestionById(Long id) {
        return questionRepository.findById(id).map(entityMapper::toQuestionDTO);
    }

    @Override
    public QuestionDTO saveQuestion(QuestionDTO questionDTO, Long idOffer) {
        Question question = entityMapper.toQuestionEntity(questionDTO);
        Offer o = offerRepository.findById(idOffer).orElse(null);
        question.setOffer(o);
        return entityMapper.toQuestionDTO(questionRepository.save(question));
    }

    @Override
    public QuestionDTO updateQuestion(Long id, QuestionDTO questionDTO) {
        Question q = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question non trouvé"));
        q.setTitle(questionDTO.getTitle());
        q.setChoice1(questionDTO.getChoice1());
        q.setChoice2(questionDTO.getChoice2());
        q.setChoice3(questionDTO.getChoice3());
        q.setResponse(questionDTO.getResponse());
        questionRepository.saveAndFlush(q);
        return entityMapper.toQuestionDTO(q);
    }

    @Override
    public void deleteQuestion(Long id) {
        if (questionRepository.existsById(id)) {
            questionRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Question non trouvé");
        }
    }
}
