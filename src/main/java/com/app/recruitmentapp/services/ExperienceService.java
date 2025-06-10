package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Experience;

import java.util.List;
import java.util.Optional;

public interface ExperienceService {
    List<Experience> getAllExperiences();

    Optional<Experience> getExperienceById(Long id);

    Experience saveExperience(Experience experience, Long idCandidate);

    Experience updateExperience(Long id, Experience experience);

    void deleteExperience(Long id);
}
