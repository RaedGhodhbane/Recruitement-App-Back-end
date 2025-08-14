package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Admin;
import com.app.recruitmentapp.entities.Offer;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface OfferService {

    List<Offer> getAllOffers();

    Optional<Offer> getOfferById(Long id);

    Offer saveOffer(Offer offer, Long idRecruiter);

    Offer updateOffer(Long id, Offer offer);

    void deleteOffer(Long id);

    ResponseEntity<Resource> getFile(String filename);
}
