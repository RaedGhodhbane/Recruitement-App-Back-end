package com.app.recruitmentapp.controllers;
import com.app.recruitmentapp.dto.FavouriteDTO;
import com.app.recruitmentapp.services.FavouriteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saved-jobs")
public class FavouriteController {
    private final FavouriteService favouriteService;

    public FavouriteController(FavouriteService favouriteService) {
        this.favouriteService = favouriteService;
    }

    @PostMapping("/{userId}/{offerId}")
    public FavouriteDTO favourite(@PathVariable Long userId, @PathVariable Long offerId) {
        return favouriteService.saveJob(userId, offerId);
    }

    @GetMapping("/{userId}")
    public List<FavouriteDTO> getSavedJobs(@PathVariable Long userId) {
        return favouriteService.getSavedJobs(userId);
    }

    @DeleteMapping("/{savedJobId}")
    public void removeSavedJob(@PathVariable Long savedJobId) {
        favouriteService.removeSavedJob(savedJobId);
    }
}
