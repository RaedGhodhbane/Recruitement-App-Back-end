package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.RecruiterDTO;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface RecruiterService {
    List<RecruiterDTO> getAllRecruiters();
    Optional<RecruiterDTO> getRecruiterById(Long id);

    RecruiterDTO registerRecruiter(String email, String rawPassword, String name, String firstName, String phone);

    RecruiterDTO addRecruiterWithPicture(RecruiterDTO recruiterDTO, MultipartFile imageFile);

    RecruiterDTO updateRecruiter(Long id, RecruiterDTO recruiterDTO);

    void deleteRecruiter(Long id);

    ResponseEntity<Resource> getFile(String filename);
}
