package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.dto.SkillDTO;
import com.app.recruitmentapp.services.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<SkillDTO> addSkill(@RequestBody SkillDTO skillDTO, @PathVariable Long idCandidate) {
        SkillDTO saved = skillService.saveSkill(skillDTO, idCandidate);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Modifier une compétence", description = "Met à jour une compétence existante")
    @PutMapping("/{id}")
    public ResponseEntity<SkillDTO> updateSkill(@PathVariable Long id, @RequestBody SkillDTO skillDTO) {
        try {
            SkillDTO updated = skillService.updateSkill(id, skillDTO);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Supprimer une compétence", description = "Supprime une compétence par son ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,String>> deleteSkill(@PathVariable Long id) {
        try {
            skillService.deleteSkill(id);
            return ResponseEntity.ok(Collections.singletonMap("message", "Skill deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
