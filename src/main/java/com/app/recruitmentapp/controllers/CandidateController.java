package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.services.CandidateService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/candidate")
public class CandidateController {
    @Autowired
    private CandidateService candidateService;

    @GetMapping("/candidates")
    public List<Candidate> getAllCandidates() {
        return candidateService.getAllCandidates();
    }

    @GetMapping("{id}")
    public ResponseEntity<Candidate> getCandidateById(@PathVariable Long id) {
        Optional<Candidate> candidate = candidateService.getCandidateById(id);
        return candidate.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping("/addCandidate1")
    public ResponseEntity<Candidate> addCandidateWithoutPicture(@RequestBody Candidate candidate) {
        Candidate savedCandidate = candidateService.saveCandidateWithoutPicture(candidate);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCandidate);
    }

    @PostMapping("/addCandidate2")
    public ResponseEntity<Candidate> addCandidateWithPicture(
            @ModelAttribute Candidate candidate,
            @RequestParam("imageFile") MultipartFile imageFile) {

        Candidate savedCandidate = candidateService.saveCandidateWithPicture(candidate, imageFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCandidate);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Candidate> updateCandidate(@PathVariable Long id, @RequestBody Candidate candidate) {
        try {
            Candidate updatedCandidate = candidateService.updateCandidate(id, candidate);
            return ResponseEntity.ok(updatedCandidate);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCandidate(@PathVariable Long id) {
        try {
            candidateService.deleteCandidate(id);
            return ResponseEntity.ok("Candidate deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/{id}/cv")
    public ResponseEntity<String> createCV(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            candidateService.createCV(id, file);
            return ResponseEntity.ok("CV uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading CV: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/cv")
    public ResponseEntity<byte[]> downloadCVPDF(@PathVariable Long id) {
        try {
            byte[] cvData = candidateService.downloadCVPDF(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=cv_" + id + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(cvData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
