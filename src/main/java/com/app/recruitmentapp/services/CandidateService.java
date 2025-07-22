package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Candidate;
import org.apache.coyote.Response;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CandidateService {
    List<Candidate> getAllCandidates();

    Optional<Candidate> getCandidateById(Long id);

    //Candidate saveCandidateWithoutPicture(Candidate candidate);

    Candidate registerCandidate(String email, String rawPassword);


    Candidate saveCandidateWithPicture(Candidate candidate, MultipartFile imageFile);

    Candidate updateCandidate(Long id, Candidate candidate);

    void deleteCandidate(Long id);

    void createCV(Long candidateId, MultipartFile cvFile) throws IOException;

    byte[] downloadCVPDF(Long candidateId) throws IOException;

    ResponseEntity<Resource> getFile(String filename);




}
