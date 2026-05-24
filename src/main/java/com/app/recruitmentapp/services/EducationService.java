package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.EducationDTO;

import java.util.List;
import java.util.Optional;

public interface EducationService {
    List<EducationDTO> getAllEducations();

    Optional<EducationDTO> getEducationById(Long id);

    EducationDTO saveEducation(EducationDTO educationDTO, Long idCandidate);

    EducationDTO updateEducation(Long id, EducationDTO educationDTO);

    void deleteEducation(Long id);
}
