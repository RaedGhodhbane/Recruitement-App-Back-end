package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.OfferDTO;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface OfferService {

    List<OfferDTO> getAllOffers();

    Optional<OfferDTO> getOfferById(Long id);

    OfferDTO saveOffer(OfferDTO offerDTO, Long idRecruiter);

    OfferDTO updateOffer(Long id, OfferDTO offerDTO);

    void deleteOffer(Long id);

    ResponseEntity<Resource> getFile(String filename);

    void deleteOfferExpired();
}
