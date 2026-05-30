package com.app.recruitmentapp.controllers;
import com.app.recruitmentapp.dto.FavouriteDTO;
import com.app.recruitmentapp.services.FavouriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Favoris", description = "Gestion des offres sauvegardées")
@RestController
@RequestMapping("/api/saved-jobs")
@PreAuthorize("isAuthenticated()")
public class FavouriteController {
    private final FavouriteService favouriteService;

    public FavouriteController(FavouriteService favouriteService) {
        this.favouriteService = favouriteService;
    }

    @Operation(summary = "Sauvegarder une offre", description = "Ajoute une offre aux favoris d'un utilisateur")
    @PostMapping("/{userId}/{offerId}")
    public FavouriteDTO favourite(@PathVariable Long userId, @PathVariable Long offerId) {
        return favouriteService.saveJob(userId, offerId);
    }

    @Operation(summary = "Offres sauvegardées", description = "Retourne les offres sauvegardées d'un utilisateur")
    @GetMapping("/{userId}")
    public List<FavouriteDTO> getSavedJobs(@PathVariable Long userId) {
        return favouriteService.getSavedJobs(userId);
    }

    @Operation(summary = "Supprimer un favori", description = "Supprime une offre des favoris")
    @DeleteMapping("/{savedJobId}")
    public void removeSavedJob(@PathVariable Long savedJobId) {
        favouriteService.removeSavedJob(savedJobId);
    }
}
