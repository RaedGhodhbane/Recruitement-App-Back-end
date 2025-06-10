package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Education;

import java.util.List;
import java.util.Optional;

public interface EducationService {
    List<Education> getAllEducations();

    Optional<Education> getEducationById(Long id);

    Education saveEducation(Education education, Long idCandidate);

    Education updateEducation(Long id, Education education);

    void deleteEducation(Long id);

}
