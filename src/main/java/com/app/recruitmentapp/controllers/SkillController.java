package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.dto.SkillDTO;
import com.app.recruitmentapp.services.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Compétences", description = "Gestion des compétences des candidats")
@RestController
@RequestMapping("/skill")
@PreAuthorize("hasRole('CANDIDATE')")
public class SkillController {
    @Autowired
    private SkillService skillService;

    @Operation(summary = "Liste des compétences", description = "Retourne toutes les compétences")
    @GetMapping("/skills")
    public List<SkillDTO> getAllSkills() {
        return skillService.getAllSkills();
    }

    @Operation(summary = "Détail d'une compétence", description = "Retourne une compétence par son ID")
    @GetMapping("/{id}")
    public ResponseEntity<SkillDTO> getSkillById(@PathVariable Long id) {
        return skillService.getSkillById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Ajouter une compétence", description = "Ajoute une compétence à un candidat")
    @PostMapping("/{idCandidate}")
    public ResponseEntity<SkillDTO> addSkill(@Valid @RequestBody SkillDTO skillDTO, @PathVariable Long idCandidate) {
        SkillDTO saved = skillService.saveSkill(skillDTO, idCandidate);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Modifier une compétence", description = "Met à jour une compétence existante")
    @PutMapping("/{id}")
    public SkillDTO updateSkill(@PathVariable Long id, @Valid @RequestBody SkillDTO skillDTO) {
        return skillService.updateSkill(id, skillDTO);
    }

    @Operation(summary = "Supprimer une compétence", description = "Supprime une compétence par son ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        skillService.deleteSkill(id);
        return ResponseEntity.ok().build();
    }
}
