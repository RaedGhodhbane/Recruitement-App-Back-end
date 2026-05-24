package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.OfferDTO;
import com.app.recruitmentapp.entities.Offer;
import com.app.recruitmentapp.entities.Recruiter;
import com.app.recruitmentapp.mapper.EntityMapper;
import com.app.recruitmentapp.repositories.OfferRepository;
import com.app.recruitmentapp.repositories.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OfferServiceImpl implements OfferService {
    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public List<OfferDTO> getAllOffers() {
        return entityMapper.toOfferDTOList(offerRepository.findAll());
    }

    @Override
    public Optional<OfferDTO> getOfferById(Long id) {
        return offerRepository.findById(id).map(entityMapper::toOfferDTO);
    }

    @Override
    public OfferDTO saveOffer(OfferDTO offerDTO, Long idRecruiter) {
        Offer offer = entityMapper.toOfferEntity(offerDTO);
        Recruiter r = recruiterRepository.findById(idRecruiter).orElse(null);
        offer.setRecruiter(r);
        return entityMapper.toOfferDTO(offerRepository.save(offer));
    }

    @Override
    public OfferDTO updateOffer(Long id, OfferDTO offerDTO) {
        Offer o = offerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offre non trouvé"));
        o.setTitle(offerDTO.getTitle());
        o.setDescription(offerDTO.getDescription());
        o.setType(offerDTO.getType());
        o.setAddress(offerDTO.getAddress());
        o.setSalary(offerDTO.getSalary());
        o.setExperience(offerDTO.getExperience());
        o.setPublicationDate(offerDTO.getPublicationDate());
        o.setExpirationDate(offerDTO.getExpirationDate());
        offerRepository.saveAndFlush(o);
        return entityMapper.toOfferDTO(o);
    }

    @Override
    public void deleteOffer(Long id) {
        if (offerRepository.existsById(id)) {
            offerRepository.deleteById(id);
        } else {
            throw new RuntimeException("Offre non trouvé");
        }
    }

    @Override
    public ResponseEntity<Resource> getFile(String filename) {
        Path file = Paths.get(uploadDir).resolve(filename);
        Resource resource;
        try {
            resource = new UrlResource(file.toUri());
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        }
        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteOfferExpired() {
        LocalDate aujourdHui = LocalDate.now();
        offerRepository.deleteByExpirationDateBefore(aujourdHui);
        System.out.println("Offer expired at" + aujourdHui);
    }
}
