package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.dto.CandidateDTO;
import com.app.recruitmentapp.entities.ChangePassword;
import com.app.recruitmentapp.services.CandidateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@Tag(name = "Candidats", description = "Gestion des candidats")
@CrossOrigin("*")
@RestController
@RequestMapping("/candidate")
public class CandidateController {
    @Autowired
    private CandidateService candidateService;

    @Operation(summary = "Liste des candidats", description = "Retourne tous les candidats")
    @GetMapping("/candidates")
    public List<CandidateDTO> getAllCandidates() {
        return candidateService.getAllCandidates();
    }

    @Operation(summary = "Détail d'un candidat", description = "Retourne un candidat par son ID")
    @GetMapping("/{id}")
    public ResponseEntity<CandidateDTO> getCandidateById(@PathVariable Long id) {
        return candidateService.getCandidateById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Inscription candidat", description = "Inscrit un nouveau candidat")
    @PostMapping("/registerCandidate")
    public CandidateDTO registerCandidate(@RequestBody Map<String, String> request) {
        return candidateService.registerCandidate(
                request.get("email"), request.get("password"),
                request.get("name"), request.get("firstName"), request.get("phone"));
    }

    @Operation(summary = "Ajouter candidat avec photo", description = "Ajoute un candidat avec une image de profil")
    @PreAuthorize("hasRole('CANDIDATE')")
    @PostMapping("/addCandidate2")
    public ResponseEntity<CandidateDTO> addCandidateWithPicture(
            @ModelAttribute CandidateDTO candidateDTO,
            @RequestParam("imageFile") MultipartFile imageFile) {
        CandidateDTO saved = candidateService.saveCandidateWithPicture(candidateDTO, imageFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Modifier un candidat", description = "Met à jour un candidat existant")
    @PreAuthorize("hasRole('CANDIDATE')")
    @PutMapping("/update/{id}")
    public CandidateDTO updateCandidate(@PathVariable Long id, @Valid @RequestBody CandidateDTO candidateDTO) {
        return candidateService.updateCandidate(id, candidateDTO);
    }

    @Operation(summary = "Supprimer un candidat", description = "Supprime un candidat par son ID")
    @PreAuthorize("hasRole('CANDIDATE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
        candidateService.deleteCandidate(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Upload CV", description = "Télécharge un fichier CV pour un candidat")
    @PreAuthorize("hasRole('CANDIDATE')")
    @PostMapping("/{id}/cv")
    public ResponseEntity<Void> createCV(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws java.io.IOException {
        candidateService.createCV(id, file);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Télécharger CV", description = "Télécharge le CV d'un candidat au format PDF")
    @GetMapping("/{id}/cv")
    public ResponseEntity<byte[]> downloadCVPDF(@PathVariable Long id) throws java.io.IOException {
        byte[] cvData = candidateService.downloadCVPDF(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=cv_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(cvData);
    }

    @Operation(summary = "Fichier candidat", description = "Retourne un fichier attaché à un candidat")
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        ResponseEntity<Resource> resource = candidateService.getFile(filename);
        return ResponseEntity.ok(resource.getBody());
    }

    @Operation(summary = "Changer mot de passe", description = "Change le mot de passe d'un candidat")
    @PreAuthorize("hasRole('CANDIDATE')")
    @PutMapping("/{id}/change-password")
    public String changePassword(
            @PathVariable Long id,
            @Valid @RequestBody ChangePassword changePasswordRequest) {
        return candidateService.changePassword(id, changePasswordRequest);
    }
}
