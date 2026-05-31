package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.dto.CandidacyDTO;
import com.app.recruitmentapp.services.CandidacyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Candidatures", description = "Gestion des candidatures aux offres")
@RestController
@RequestMapping("/candidacy")
public class CandidacyController {
    @Autowired
    private CandidacyService candidacyService;

    @Operation(summary = "Liste des candidatures", description = "Retourne toutes les candidatures")
    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/candidacies")
    public List<CandidacyDTO> getAllCandidacies() {
        return candidacyService.getAllCandidacies();
    }

    @Operation(summary = "Détail d'une candidature", description = "Retourne une candidature par son ID")
    @PreAuthorize("hasAnyRole('CANDIDATE', 'RECRUITER')")
    @GetMapping("/{id}")
    public ResponseEntity<CandidacyDTO> getCandidacyById(@PathVariable Long id) {
        return candidacyService.getCandidacyById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Postuler", description = "Soumet une nouvelle candidature")
    @PreAuthorize("hasRole('CANDIDATE')")
    @PostMapping()
    public ResponseEntity<CandidacyDTO> addCandidacy(@RequestBody CandidacyDTO candidacyDTO) {
        CandidacyDTO saved = candidacyService.saveCandidacy(candidacyDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Modifier une candidature", description = "Met à jour une candidature existante")
    @PreAuthorize("hasRole('CANDIDATE')")
    @PutMapping("/{id}")
    public CandidacyDTO updateCandidacy(@PathVariable Long id, @RequestBody CandidacyDTO candidacyDTO) {
        return candidacyService.updateCandidacy(id, candidacyDTO);
    }

    @Operation(summary = "Supprimer une candidature", description = "Supprime une candidature par son ID")
    @PreAuthorize("hasRole('CANDIDATE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidacy(@PathVariable Long id) {
        candidacyService.deleteCandidacy(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Accepter candidature", description = "Accepte une candidature")
    @PreAuthorize("hasRole('RECRUITER')")
    @PutMapping("/{id}/accept")
    public ResponseEntity<Void> acceptApplication(@PathVariable Long id) {
        candidacyService.acceptApplication(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Refuser candidature", description = "Refuse une candidature")
    @PreAuthorize("hasRole('RECRUITER')")
    @PutMapping("/{id}/decline")
    public ResponseEntity<Void> declineApplication(@PathVariable Long id) {
        candidacyService.declineApplication(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Vérifier candidature", description = "Vérifie si une candidature existe déjà")
    @PreAuthorize("hasRole('CANDIDATE')")
    @GetMapping("/candidacies/exists")
    public boolean existsCandidacy(@RequestParam Long offerId, @RequestParam Long candidateId) {
        return candidacyService.candidacyExists(offerId, candidateId);
    }
}
