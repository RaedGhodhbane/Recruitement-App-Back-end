package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.FavouriteDTO;

import java.util.List;

public interface FavouriteService {
    FavouriteDTO saveJob(Long userId, Long offerId);
    List<FavouriteDTO> getSavedJobs(Long userId);
    void removeSavedJob(Long savedJobId);
}
