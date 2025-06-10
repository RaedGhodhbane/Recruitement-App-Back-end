package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.Education;
import com.app.recruitmentapp.repositories.CandidateRepository;
import com.app.recruitmentapp.repositories.EducationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EducationServiceImpl implements EducationService {
    @Autowired
    private EducationRepository educationRepository;
    @Autowired
    private CandidateRepository candidateRepository;
    @Override
    public List<Education> getAllEducations() {
        return educationRepository.findAll();
    }

    @Override
    public Optional<Education> getEducationById(Long id) {
        return educationRepository.findById(id);
    }

    @Override
    public Education saveEducation(Education education, Long idCandidate) {
        Candidate c = candidateRepository.findById(idCandidate).orElse(null);
        education.setCandidate(c);
        return educationRepository.save(education);
    }

    @Override
    public Education updateEducation(Long id, Education newEducation) {
        Education e = educationRepository.findById(id).orElse(null);
        e.setDiploma(newEducation.getDiploma());
        e.setUniversity(newEducation.getUniversity());
        e.setEndDate(newEducation.getEndDate());
        e.setCandidate(newEducation.getCandidate());
        educationRepository.saveAndFlush(e);
        return e;
    }

    @Override
    public void deleteEducation(Long id) {
        if (educationRepository.existsById(id)) {
            educationRepository.deleteById(id);
        } else {
            throw new RuntimeException("Candidat non trouvé");
        }
    }


}
