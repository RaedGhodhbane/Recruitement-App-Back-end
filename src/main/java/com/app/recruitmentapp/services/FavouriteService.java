package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Favourite;
import com.app.recruitmentapp.entities.Offer;
import com.app.recruitmentapp.entities.User;

import java.util.List;

public interface FavouriteService {
    Favourite saveJob(User user, Offer offer);
    List<Favourite> getSavedJobs(Long userId);
    void removeSavedJob(Long savedJobId);
}
