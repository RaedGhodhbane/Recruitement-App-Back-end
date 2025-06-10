package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.entities.Candidacy;
import com.app.recruitmentapp.services.CandidacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/candidacy")
public class CandidacyController {
    @Autowired
    private CandidacyService candidacyService;

    @GetMapping("/candidacies")
    public List<Candidacy> getAllCandidacies() {
        return candidacyService.getAllCandidacies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Candidacy> getCandidacyById(@PathVariable Long id) {
        Optional<Candidacy> candidacy = candidacyService.getCandidacyById(id);
        return candidacy.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Candidacy> addCandidacy(@RequestBody Candidacy candidacy) {
        Candidacy savedCandidacy = candidacyService.saveCandidacy(candidacy);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCandidacy);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Candidacy> updateCandidacy(@PathVariable Long id, @RequestBody Candidacy candidacy) {
        try {
            Candidacy updatedCandidacy = candidacyService.updateCandidacy(id, candidacy);
            return ResponseEntity.ok(updatedCandidacy);
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
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @PutMapping("/{id}/decline")
    public ResponseEntity<Void> declineApplication(@PathVariable Long id) {
        candidacyService.declineApplication(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

}
