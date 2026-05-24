package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.dto.OfferDTO;
import com.app.recruitmentapp.services.OfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Offres", description = "Gestion des offres d'emploi")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/offer")
public class OfferController {
    @Autowired
    private OfferService offerService;

    @Operation(summary = "Liste des offres", description = "Retourne toutes les offres d'emploi")
    @GetMapping("/offers")
    public List<OfferDTO> getAllOffers() {
        return offerService.getAllOffers();
    }

    @Operation(summary = "Détail d'une offre", description = "Retourne une offre par son ID")
    @GetMapping("/{id}")
    public ResponseEntity<OfferDTO> getOfferById(@PathVariable Long id) {
        return offerService.getOfferById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Créer une offre", description = "Ajoute une nouvelle offre (recruteur uniquement)")
    @PreAuthorize("hasRole('RECRUITER')")
    @PostMapping(path = "/{idRecruiter}")
    public ResponseEntity<OfferDTO> addOffer(
            @RequestBody OfferDTO offerDTO,
            @PathVariable Long idRecruiter) {
        OfferDTO saved = offerService.saveOffer(offerDTO, idRecruiter);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Modifier une offre", description = "Met à jour une offre existante")
    @PutMapping("/{id}")
    public ResponseEntity<OfferDTO> updateOffer(@PathVariable Long id, @RequestBody OfferDTO offerDTO) {
        try {
            OfferDTO updated = offerService.updateOffer(id, offerDTO);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Supprimer une offre", description = "Supprime une offre par son ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOffer(@PathVariable Long id) {
        try {
            offerService.deleteOffer(id);
            return ResponseEntity.ok("Offer deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Télécharger un fichier", description = "Retourne un fichier attaché à une offre")
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        ResponseEntity<Resource> resource = offerService.getFile(filename);
        return ResponseEntity.ok(resource.getBody());
    }
}
