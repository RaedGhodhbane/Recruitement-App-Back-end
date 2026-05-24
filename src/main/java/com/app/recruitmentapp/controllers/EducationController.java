package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.dto.EducationDTO;
import com.app.recruitmentapp.services.EducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/education")
public class EducationController {
    @Autowired
    private EducationService educationService;

    @GetMapping("/educations")
    public List<EducationDTO> getAllEducations() {
        return educationService.getAllEducations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EducationDTO> getEducationById(@PathVariable Long id) {
        return educationService.getEducationById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{idCandidate}")
    public ResponseEntity<EducationDTO> addEducation(@RequestBody EducationDTO educationDTO, @PathVariable Long idCandidate) {
        EducationDTO saved = educationService.saveEducation(educationDTO, idCandidate);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EducationDTO> updateEducation(@PathVariable Long id, @RequestBody EducationDTO educationDTO) {
        try {
            EducationDTO updated = educationService.updateEducation(id, educationDTO);
            return ResponseEntity.ok(updated);
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
