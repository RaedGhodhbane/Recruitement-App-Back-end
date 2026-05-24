package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.dto.ExperienceDTO;
import com.app.recruitmentapp.services.ExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/experience")
public class ExperienceController {
    @Autowired
    private ExperienceService experienceService;

    @GetMapping("/experiences")
    public List<ExperienceDTO> getAllExperiences() {
        return experienceService.getAllExperiences();
    }

    @GetMapping("{id}")
    public ResponseEntity<ExperienceDTO> getExperienceById(@PathVariable Long id) {
        return experienceService.getExperienceById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{idCandidate}")
    public ResponseEntity<ExperienceDTO> addExperience(@RequestBody ExperienceDTO experienceDTO, @PathVariable Long idCandidate) {
        ExperienceDTO saved = experienceService.saveExperience(experienceDTO, idCandidate);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExperienceDTO> updateExperience(@PathVariable Long id, @RequestBody ExperienceDTO experienceDTO) {
        try {
            ExperienceDTO updated = experienceService.updateExperience(id, experienceDTO);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,String>> deleteExperience(@PathVariable Long id) {
        try {
            experienceService.deleteExperience(id);
            return ResponseEntity.ok(Collections.singletonMap("message","Experience deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
