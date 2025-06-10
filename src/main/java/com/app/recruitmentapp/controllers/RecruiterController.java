package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.entities.Recruiter;
import com.app.recruitmentapp.services.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/recruiter")
public class RecruiterController {
    @Autowired
    private RecruiterService recruiterService;

    @GetMapping("/recruiters")
    public List<Recruiter> getAllRecruiters() {
        return recruiterService.getAllRecruiters();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recruiter> getRecruiterById(@PathVariable Long id) {
        Optional<Recruiter> recruiter = recruiterService.getRecruiterById(id);
        return recruiter.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Recruiter> addRecruiter(@RequestBody Recruiter recruiter) {
        Recruiter savedRecruiter = recruiterService.saveRecruiter(recruiter);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecruiter);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recruiter> updateRecruiter(@PathVariable Long id, @RequestBody Recruiter recruiter) {
        try {
            Recruiter updatedRecruiter = recruiterService.updateRecruiter(id, recruiter);
            return ResponseEntity.ok(updatedRecruiter);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecruiter(@PathVariable Long id) {
        try {
            recruiterService.deleteRecruiter(id);
            return ResponseEntity.ok("Recruiter deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
