package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Offer;
import com.app.recruitmentapp.entities.Recruiter;
import com.app.recruitmentapp.repositories.OfferRepository;
import com.app.recruitmentapp.repositories.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OfferServiceImpl implements OfferService {
    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Override
    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    @Override
    public Optional<Offer> getOfferById(Long id) {
        return offerRepository.findById(id);
    }

    @Override
    public Offer saveOffer(Offer offer, Long idRecruiter) {
        Recruiter r = recruiterRepository.findById(idRecruiter).orElse(null);
        offer.setRecruiter(r);
        return offerRepository.save(offer);
    }

    @Override
    public Offer updateOffer(Long id, Offer newOffer) {
        Offer o = offerRepository.findById(id).orElse(null);
        o.setTitle(newOffer.getTitle());
        o.setDescription(newOffer.getDescription());
        o.setType(newOffer.getType());
        o.setAddress(newOffer.getAddress());
        o.setSalaire(newOffer.getSalaire());
        o.setExperience(newOffer.getExperience());
        o.setPublicationDate(newOffer.getPublicationDate());
        o.setExpirationDate(newOffer.getExpirationDate());
        o.setCandidacyList(newOffer.getCandidacyList());
        o.setQuestionList(newOffer.getQuestionList());
        o.setRecruiter(newOffer.getRecruiter());
        offerRepository.saveAndFlush(o);
        return o;
    }

    @Override
    public void deleteOffer(Long id) {
        if (offerRepository.existsById(id)) {
            offerRepository.deleteById(id);
        } else {
            throw new RuntimeException("Candidat non trouvé");
        }
    }
}
