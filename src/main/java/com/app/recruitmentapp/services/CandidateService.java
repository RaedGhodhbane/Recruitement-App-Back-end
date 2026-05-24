package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.CandidateDTO;
import com.app.recruitmentapp.entities.ChangePassword;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface CandidateService {
    List<CandidateDTO> getAllCandidates();

    Optional<CandidateDTO> getCandidateById(Long id);

    CandidateDTO registerCandidate(String email, String rawPassword, String name, String firstName, String phone);

    CandidateDTO saveCandidateWithPicture(CandidateDTO candidateDTO, MultipartFile imageFile);

    CandidateDTO updateCandidate(Long id, CandidateDTO candidateDTO);

    void deleteCandidate(Long id);

    void createCV(Long candidateId, MultipartFile cvFile) throws IOException;

    byte[] downloadCVPDF(Long candidateId) throws IOException;

    ResponseEntity<Resource> getFile(String filename);

    String changePassword(Long id, ChangePassword changePasswordRequest);
}
