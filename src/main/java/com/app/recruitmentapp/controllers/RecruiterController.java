package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.dto.RecruiterDTO;
import com.app.recruitmentapp.services.RecruiterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Tag(name = "Recruteurs", description = "Gestion des recruteurs")
@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/recruiter")
public class RecruiterController {
    @Autowired
    private RecruiterService recruiterService;

    @Operation(summary = "Liste des recruteurs", description = "Retourne tous les recruteurs")
    @GetMapping("/recruiters")
    public List<RecruiterDTO> getAllRecruiters() {
        return recruiterService.getAllRecruiters();
    }

    @Operation(summary = "Détail d'un recruteur", description = "Retourne un recruteur par son ID")
    @GetMapping("/{id}")
    public ResponseEntity<RecruiterDTO> getRecruiterById(@PathVariable Long id) {
        return recruiterService.getRecruiterById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Inscription recruteur", description = "Inscrit un nouveau recruteur")
    @PostMapping("/registerRecruiter")
    public ResponseEntity<?> registerRecruiter(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        String name = request.get("name");
        String firstName = request.get("firstName");
        String phone = request.get("phone");

        try {
            RecruiterDTO saved = recruiterService.registerRecruiter(email, password, name, firstName, phone);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Ajouter recruteur avec photo", description = "Ajoute un recruteur avec une image de profil")
    @PreAuthorize("hasRole('RECRUITER')")
    @PostMapping("/addRecruiter2")
    public ResponseEntity<RecruiterDTO> addRecruiterWithPicture(
            @ModelAttribute RecruiterDTO recruiterDTO,
            @RequestParam("imageFile") MultipartFile imageFile) {
        RecruiterDTO saved = recruiterService.addRecruiterWithPicture(recruiterDTO, imageFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Modifier un recruteur", description = "Met à jour un recruteur existant")
    @PreAuthorize("hasRole('RECRUITER')")
    @PutMapping("/{id}")
    public ResponseEntity<RecruiterDTO> updateRecruiter(@PathVariable Long id, @RequestBody RecruiterDTO recruiterDTO) {
        try {
            RecruiterDTO updated = recruiterService.updateRecruiter(id, recruiterDTO);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Supprimer un recruteur", description = "Supprime un recruteur par son ID")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecruiter(@PathVariable Long id) {
        try {
            recruiterService.deleteRecruiter(id);
            return ResponseEntity.ok("Recruiter deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Fichier recruteur", description = "Retourne un fichier attaché à un recruteur")
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        ResponseEntity<Resource> resource = recruiterService.getFile(filename);
        return ResponseEntity.ok(resource.getBody());
    }
}
