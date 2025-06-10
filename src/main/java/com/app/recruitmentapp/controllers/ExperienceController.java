package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.entities.Experience;
import com.app.recruitmentapp.services.ExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/experience")
public class ExperienceController {
    @Autowired
    private ExperienceService experienceService;

    @GetMapping("/experiences")
    public List<Experience> getAllExperiences() {
        return experienceService.getAllExperiences();
    }

    @GetMapping("{id}")
    public ResponseEntity<Experience> getExperienceById(@PathVariable Long id) {
        Optional<Experience> experience = experienceService.getExperienceById(id);
        return experience.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{idCandidate}")
    public ResponseEntity<Experience> addExperience(@RequestBody Experience experience, @PathVariable Long idCandidate) {
        Experience savedExperience = experienceService.saveExperience(experience,idCandidate);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedExperience);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Experience> updateExperience(@PathVariable Long id, @RequestBody Experience experience) {
        try {
            Experience updatedExperience = experienceService.updateExperience(id, experience);
            return ResponseEntity.ok(updatedExperience);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExperience(@PathVariable Long id) {
        try {
            experienceService.deleteExperience(id);
            return ResponseEntity.ok("Experience deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
