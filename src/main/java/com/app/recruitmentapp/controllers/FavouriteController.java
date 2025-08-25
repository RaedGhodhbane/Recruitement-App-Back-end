package com.app.recruitmentapp.controllers;
import com.app.recruitmentapp.entities.Favourite;
import com.app.recruitmentapp.entities.Offer;
import com.app.recruitmentapp.entities.User;
import com.app.recruitmentapp.repositories.OfferRepository;
import com.app.recruitmentapp.repositories.UserRepository;
import com.app.recruitmentapp.services.FavouriteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saved-jobs")
public class FavouriteController {
    private final FavouriteService favouriteService;
    private final UserRepository userRepository;
    private final OfferRepository offerRepository;

    public FavouriteController(FavouriteService favouriteService, UserRepository userRepository, OfferRepository offerRepository) {
        this.favouriteService = favouriteService;
        this.userRepository = userRepository;
        this.offerRepository = offerRepository;
    }

    @PostMapping("/{userId}/{offerId}")
    public Favourite favourite(@PathVariable Long userId, @PathVariable Long offerId) {
        User user = userRepository.findById(userId).orElseThrow();
        Offer offer = offerRepository.findById(offerId).orElseThrow();
        return favouriteService.saveJob(user, offer);
    }

    @GetMapping("/{userId}")
    public List<Favourite> getSavedJobs(@PathVariable Long userId) {
        return favouriteService.getSavedJobs(userId);
    }

    @DeleteMapping("/{savedJobId}")
    public void removeSavedJob(@PathVariable Long savedJobId) {
        favouriteService.removeSavedJob(savedJobId);
    }
}