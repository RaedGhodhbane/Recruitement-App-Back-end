package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.ExperienceDTO;

import java.util.List;
import java.util.Optional;

public interface ExperienceService {
    List<ExperienceDTO> getAllExperiences();

    Optional<ExperienceDTO> getExperienceById(Long id);

    ExperienceDTO saveExperience(ExperienceDTO experienceDTO, Long idCandidate);

    ExperienceDTO updateExperience(Long id, ExperienceDTO experienceDTO);

    void deleteExperience(Long id);
}
