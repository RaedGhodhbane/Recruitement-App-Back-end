package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.dto.ExperienceDTO;
import com.app.recruitmentapp.services.ExperienceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Expériences", description = "Gestion des expériences professionnelles des candidats")
@RestController
@RequestMapping("/experience")
@PreAuthorize("hasRole('CANDIDATE')")
public class ExperienceController {
    @Autowired
    private ExperienceService experienceService;

    @Operation(summary = "Liste des expériences", description = "Retourne toutes les expériences")
    @GetMapping("/experiences")
    public List<ExperienceDTO> getAllExperiences() {
        return experienceService.getAllExperiences();
    }

    @Operation(summary = "Détail d'une expérience", description = "Retourne une expérience par son ID")
    @GetMapping("{id}")
    public ResponseEntity<ExperienceDTO> getExperienceById(@PathVariable Long id) {
        return experienceService.getExperienceById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Ajouter une expérience", description = "Ajoute une expérience à un candidat")
    @PostMapping("/{idCandidate}")
    public ResponseEntity<ExperienceDTO> addExperience(@Valid @RequestBody ExperienceDTO experienceDTO, @PathVariable Long idCandidate) {
        ExperienceDTO saved = experienceService.saveExperience(experienceDTO, idCandidate);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Modifier une expérience", description = "Met à jour une expérience existante")
    @PutMapping("/{id}")
    public ExperienceDTO updateExperience(@PathVariable Long id, @Valid @RequestBody ExperienceDTO experienceDTO) {
        return experienceService.updateExperience(id, experienceDTO);
    }

    @Operation(summary = "Supprimer une expérience", description = "Supprime une expérience par son ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExperience(@PathVariable Long id) {
        experienceService.deleteExperience(id);
        return ResponseEntity.ok().build();
    }
}
