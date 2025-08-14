package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.entities.Education;
import com.app.recruitmentapp.services.EducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/education")
public class EducationController {
    @Autowired
    private EducationService educationService;

    @GetMapping("/educations")
    public List<Education> getAllEducations() {
        return educationService.getAllEducations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Education> getEducationById(@PathVariable Long id) {
        Optional<Education> education = educationService.getEducationById(id);
        return education.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{idCandidate}")
    public ResponseEntity<Education> addEducation(@RequestBody Education education, @PathVariable Long idCandidate) {
        Education savedEducation = educationService.saveEducation(education, idCandidate);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEducation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Education> updateEducation(@PathVariable Long id, @RequestBody Education education) {
        try {
            Education updatedEducation = educationService.updateEducation(id, education);
            return ResponseEntity.ok(updatedEducation);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteEducation(@PathVariable Long id) {
        try {
            educationService.deleteEducation(id);
            return ResponseEntity.ok(Collections.singletonMap("message", "Education deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
