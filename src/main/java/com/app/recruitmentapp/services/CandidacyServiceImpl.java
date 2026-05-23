package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Candidacy;
import com.app.recruitmentapp.entities.Status;
import com.app.recruitmentapp.repositories.CandidacyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidacyServiceImpl implements CandidacyService {

    private final CandidacyRepository candidacyRepository;

    public CandidacyServiceImpl(CandidacyRepository candidacyRepository) {
        this.candidacyRepository = candidacyRepository;
    }

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
        Candidacy c = candidacyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Candidacy not found with id " + id));
        c.setSubmissionDate(newCandidacy.getSubmissionDate());
        c.setStatus(newCandidacy.getStatus());
        c.setScore(newCandidacy.getScore());
        c.setCandidate(newCandidacy.getCandidate());
        c.setOffer(newCandidacy.getOffer());
        return candidacyRepository.save(c);
    }

    @Override
    public void deleteCandidacy(Long id) {
        Candidacy candidacy = candidacyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Candidacy not found with id " + id));
        candidacyRepository.delete(candidacy);
    }

    @Override
    public void acceptApplication(Long id) {
        Candidacy candidacy = candidacyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Candidacy not found with id " + id));
        candidacy.setStatus(Status.ACCEPTED);
        candidacyRepository.save(candidacy);
    }

    @Override
    public void declineApplication(Long id) {
        Candidacy candidacy = candidacyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Candidacy not found with id " + id));
        candidacy.setStatus(Status.DECLINED);
        candidacyRepository.save(candidacy);
    }

    @Override
    public boolean candidacyExists(Long offerId, Long candidateId) {
        return candidacyRepository.existsByOfferIdAndCandidateId(offerId, candidateId);
    }

}
