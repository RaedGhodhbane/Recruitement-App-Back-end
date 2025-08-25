package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Favourite;
import com.app.recruitmentapp.entities.Offer;
import com.app.recruitmentapp.entities.User;
import com.app.recruitmentapp.repositories.FavouriteRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FavouriteServiceImpl implements FavouriteService{

    private final FavouriteRepository favouriteRepository;

    public FavouriteServiceImpl(FavouriteRepository favouriteRepository) {
        this.favouriteRepository = favouriteRepository;
    }

    public Favourite saveJob(User user, Offer offer) {
        if (favouriteRepository.existsByUserIdAndOfferId(user.getId(), offer.getId())) {
            throw new RuntimeException("Job déjà sauvegardé !");
        }
        Favourite savedJob = new Favourite();
        savedJob.setUser(user);
        savedJob.setOffer(offer);
        return favouriteRepository.save(savedJob);
    }

    public List<Favourite> getSavedJobs(Long userId) {
        return favouriteRepository.findByUserId(userId);
    }

    public void removeSavedJob(Long savedJobId) {
        favouriteRepository.deleteById(savedJobId);
    }
}

