package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.Recruiter;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface RecruiterService {
    List<Recruiter> getAllRecruiters();
    Optional<Recruiter> getRecruiterById(Long id);

    // Recruiter addRecruiterWithoutPicture(Recruiter recruiter);

    Recruiter registerRecruiter(String email, String rawPassword, String name, String firstName, String phone);

    Recruiter addRecruiterWithPicture(Recruiter recruiter, MultipartFile imageFile);

    Recruiter updateRecruiter(Long id, Recruiter recruiter);

    void deleteRecruiter(Long id);

    ResponseEntity<Resource> getFile(String filename);







}
