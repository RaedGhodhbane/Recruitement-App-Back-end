package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.dto.CandidacyDTO;
import com.app.recruitmentapp.services.CandidacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/candidacy")
public class CandidacyController {
    @Autowired
    private CandidacyService candidacyService;

    @GetMapping("/candidacies")
    public List<CandidacyDTO> getAllCandidacies() {
        return candidacyService.getAllCandidacies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidacyDTO> getCandidacyById(@PathVariable Long id) {
        return candidacyService.getCandidacyById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<CandidacyDTO> addCandidacy(@RequestBody CandidacyDTO candidacyDTO) {
        CandidacyDTO saved = candidacyService.saveCandidacy(candidacyDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CandidacyDTO> updateCandidacy(@PathVariable Long id, @RequestBody CandidacyDTO candidacyDTO) {
        try {
            CandidacyDTO updated = candidacyService.updateCandidacy(id, candidacyDTO);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCandidacy(@PathVariable Long id) {
        try {
            candidacyService.deleteCandidacy(id);
            return ResponseEntity.ok("Candidacy deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<Void> acceptApplication(@PathVariable Long id) {
        candidacyService.acceptApplication(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/decline")
    public ResponseEntity<Void> declineApplication(@PathVariable Long id) {
        candidacyService.declineApplication(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/candidacies/exists")
    public boolean existsCandidacy(@RequestParam Long offerId, @RequestParam Long candidateId) {
        return candidacyService.candidacyExists(offerId, candidateId);
    }
}
