package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.FavouriteDTO;
import com.app.recruitmentapp.entities.Favourite;
import com.app.recruitmentapp.entities.Offer;
import com.app.recruitmentapp.entities.User;
import com.app.recruitmentapp.mapper.EntityMapper;
import com.app.recruitmentapp.exceptions.DuplicateResourceException;
import com.app.recruitmentapp.repositories.FavouriteRepository;
import com.app.recruitmentapp.repositories.OfferRepository;
import com.app.recruitmentapp.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavouriteServiceImpl implements FavouriteService{

    private final FavouriteRepository favouriteRepository;
    private final UserRepository userRepository;
    private final OfferRepository offerRepository;
    private final EntityMapper entityMapper;

    public FavouriteServiceImpl(FavouriteRepository favouriteRepository, UserRepository userRepository, OfferRepository offerRepository, EntityMapper entityMapper) {
        this.favouriteRepository = favouriteRepository;
        this.userRepository = userRepository;
        this.offerRepository = offerRepository;
        this.entityMapper = entityMapper;
    }

    public FavouriteDTO saveJob(Long userId, Long offerId) {
        User user = userRepository.findById(userId).orElseThrow();
        Offer offer = offerRepository.findById(offerId).orElseThrow();
        if (favouriteRepository.existsByUserIdAndOfferId(user.getId(), offer.getId())) {
            throw new DuplicateResourceException("Job déjà sauvegardé !");
        }
        Favourite savedJob = new Favourite();
        savedJob.setUser(user);
        savedJob.setOffer(offer);
        return entityMapper.toFavouriteDTO(favouriteRepository.save(savedJob));
    }

    public List<FavouriteDTO> getSavedJobs(Long userId) {
        return entityMapper.toFavouriteDTOList(favouriteRepository.findByUserId(userId));
    }

    public void removeSavedJob(Long savedJobId) {
        favouriteRepository.deleteById(savedJobId);
    }
}
