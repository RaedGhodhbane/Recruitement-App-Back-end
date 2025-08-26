package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Candidacy;
import com.app.recruitmentapp.entities.Status;
import com.app.recruitmentapp.repositories.CandidacyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidacyServiceImpl implements CandidacyService {
    @Autowired
    private CandidacyRepository candidacyRepository;
    @Override
    public List<Candidacy> getAllCandidacies() {
        return candidacyRepository.findAll();
    }

    @Override
    public Optional<Candidacy> getCandidacyById(Long id) {
        return candidacyRepository.findById(id);
    }

    @Override
    public Candidacy saveCandidacy(Candidacy candidacy) {
        return candidacyRepository.save(candidacy);
    }

    @Override
    public Candidacy updateCandidacy(Long id, Candidacy newCandidacy) {
        Candidacy c = candidacyRepository.findById(id).orElse(null);
        c.setSubmissionDate(newCandidacy.getSubmissionDate());
        c.setStatus(newCandidacy.getStatus());
        c.setScore(newCandidacy.getScore());
        c.setCandidate(newCandidacy.getCandidate());
        c.setOffer(newCandidacy.getOffer());
        candidacyRepository.saveAndFlush(c);
        return c;
    }

    @Override
    public void deleteCandidacy(Long id) {
        if (candidacyRepository.existsById(id)) {
            candidacyRepository.deleteById(id);
        } else {
            throw new RuntimeException("Candidat non trouvé");
        }
    }

    public void acceptApplication(Long id) {
        Candidacy candidacy = candidacyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidacy not found with id " + id));
        candidacy.setStatus(Status.ACCEPTED);
        candidacyRepository.save(candidacy);
    }

    public void declineApplication(Long id) {
        Candidacy candidacy = candidacyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidacy not found with id " + id));
        candidacy.setStatus(Status.DECLINED);
        candidacyRepository.save(candidacy);
    }

    public boolean candidacyExists(Long offerId, Long candidateId) {
        return candidacyRepository.existsByOfferIdAndCandidateId(offerId, candidateId);
    }

}
