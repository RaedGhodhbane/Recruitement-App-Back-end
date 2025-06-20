package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.Recruiter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface RecruiterService {
    List<Recruiter> getAllRecruiters();
    Optional<Recruiter> getRecruiterById(Long id);

    // Recruiter addRecruiterWithoutPicture(Recruiter recruiter);

    Recruiter registerRecruiter(String email, String rawPassword);

    Recruiter addRecruiterWithPicture(Recruiter recruiter, MultipartFile imageFile);

    Recruiter updateRecruiter(Long id, Recruiter recruiter);

    void deleteRecruiter(Long id);




}
