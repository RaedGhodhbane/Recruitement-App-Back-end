package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.Experience;
import com.app.recruitmentapp.repositories.CandidateRepository;
import com.app.recruitmentapp.repositories.ExperienceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExperienceServiceImpl implements ExperienceService {
    @Autowired
    private ExperienceRepository experienceRepository;
    @Autowired
    private CandidateRepository candidateRepository;
    @Override
    public List<Experience> getAllExperiences() {
        return experienceRepository.findAll();
    }

    @Override
    public Optional<Experience> getExperienceById(Long id) {
        return experienceRepository.findById(id);
    }

    @Override
    public Experience saveExperience(Experience experience, Long idCandidate) {
        Candidate c = candidateRepository.findById(idCandidate).orElse(null);
        experience.setCandidate(c);
        return experienceRepository.save(experience);
    }

    @Override
    public Experience updateExperience(Long id, Experience newExperience) {
        Experience ex = experienceRepository.findById(id).orElse(null);
        ex.setCompanyName(newExperience.getCompanyName());
        ex.setJobTitle(newExperience.getJobTitle());
        ex.setStartExpDate(newExperience.getStartExpDate());
        ex.setEndExpDate(newExperience.getEndExpDate());
        ex.setDescription(newExperience.getDescription());
        experienceRepository.saveAndFlush(ex);
        return ex;
    }

    @Override
    public void deleteExperience(Long id) {
        if (experienceRepository.existsById(id)) {
            experienceRepository.deleteById(id);
        } else {
            throw new RuntimeException("Candidat non trouvé");
        }
    }

}
