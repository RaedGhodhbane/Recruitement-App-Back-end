package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Recruiter;

import java.util.List;
import java.util.Optional;

public interface RecruiterService {
    List<Recruiter> getAllRecruiters();
    Optional<Recruiter> getRecruiterById(Long id);

    Recruiter saveRecruiter(Recruiter recruiter);

    Recruiter updateRecruiter(Long id, Recruiter recruiter);

    void deleteRecruiter(Long id);
}
