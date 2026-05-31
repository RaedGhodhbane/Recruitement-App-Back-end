package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.dto.EducationDTO;
import com.app.recruitmentapp.services.EducationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Formations", description = "Gestion des formations des candidats")
@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/education")
@PreAuthorize("hasRole('CANDIDATE')")
public class EducationController {
    @Autowired
    private EducationService educationService;

    @Operation(summary = "Liste des formations", description = "Retourne toutes les formations")
    @GetMapping("/educations")
    public List<EducationDTO> getAllEducations() {
        return educationService.getAllEducations();
    }

    @Operation(summary = "Détail d'une formation", description = "Retourne une formation par son ID")
    @GetMapping("/{id}")
    public ResponseEntity<EducationDTO> getEducationById(@PathVariable Long id) {
        return educationService.getEducationById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Ajouter une formation", description = "Ajoute une formation à un candidat")
    @PostMapping("/{idCandidate}")
    public ResponseEntity<EducationDTO> addEducation(@RequestBody EducationDTO educationDTO, @PathVariable Long idCandidate) {
        EducationDTO saved = educationService.saveEducation(educationDTO, idCandidate);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Modifier une formation", description = "Met à jour une formation existante")
    @PutMapping("/{id}")
    public EducationDTO updateEducation(@PathVariable Long id, @RequestBody EducationDTO educationDTO) {
        return educationService.updateEducation(id, educationDTO);
    }

    @Operation(summary = "Supprimer une formation", description = "Supprime une formation par son ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEducation(@PathVariable Long id) {
        educationService.deleteEducation(id);
        return ResponseEntity.ok().build();
    }
}
