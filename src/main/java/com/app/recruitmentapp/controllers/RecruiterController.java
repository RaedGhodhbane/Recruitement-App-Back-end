package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.Recruiter;
import com.app.recruitmentapp.services.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin("http://localhost:4200")
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

    /*
    @PostMapping("/addRecruiter1")

    public ResponseEntity<Recruiter> addRecruiterWithoutPicture(@RequestBody Recruiter recruiter) {
        Recruiter savedRecruiter = recruiterService.addRecruiterWithoutPicture(recruiter);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecruiter);
    }
     */

    @PostMapping("/registerRecruiter")
    public ResponseEntity<?> registerRecruiter(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        String name = request.get("name");
        String firstName = request.get("firstName");
        String phone = request.get("phone");

        try {
            Recruiter recruiter = recruiterService.registerRecruiter(email,password, name,firstName,phone);
            return ResponseEntity.ok(recruiter);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }


    @PostMapping("/addRecruiter2")
    public ResponseEntity<Recruiter> addRecruiterWithPicture(
            @ModelAttribute Recruiter recruiter,
            @RequestParam("imageFile") MultipartFile imageFile) {

        Recruiter savedRecruiter = recruiterService.addRecruiterWithPicture(recruiter, imageFile);
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

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        ResponseEntity<Resource> resource = recruiterService.getFile(filename);
        return ResponseEntity.ok(resource.getBody());
    }
}
