package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Admin;
import com.app.recruitmentapp.entities.Offer;

import java.util.List;
import java.util.Optional;

public interface OfferService {

    List<Offer> getAllOffers();

    Optional<Offer> getOfferById(Long id);

    Offer saveOffer(Offer offer, Long idRecruiter);

    Offer updateOffer(Long id, Offer offer);

    void deleteOffer(Long id);
}
