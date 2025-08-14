package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Offer;
import com.app.recruitmentapp.entities.Recruiter;
import com.app.recruitmentapp.repositories.OfferRepository;
import com.app.recruitmentapp.repositories.RecruiterRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OfferServiceImpl implements OfferService {
    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

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
        o.setSalary(newOffer.getSalary());
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

    @Override
    public ResponseEntity<Resource> getFile(String filename) {
        // Il construit le chemin complet du fichier en combinant le dossier d’upload //(`uploadDir`) et le nom du fichier reçu.
        Path file = Paths.get(uploadDir).resolve(filename);
        Resource resource;
        try {
//Il convertit le fichier en un objet `Resource` pour pouvoir être renvoyé dans la réponse HTTP
            resource = new UrlResource(file.toUri());
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        }
        if (resource.exists() || resource.isReadable()) {
//HttpHeaders.CONTENT_DISPOSITION permet d'envoyer le fichier avec une suggestion de téléchargement.
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() +"\"").body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
