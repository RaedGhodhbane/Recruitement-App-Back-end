package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Candidacy;

import java.util.List;
import java.util.Optional;

public interface CandidacyService {

    List<Candidacy> getAllCandidacies();

    Optional<Candidacy> getCandidacyById(Long id);

    Candidacy saveCandidacy(Candidacy candidacy);

    Candidacy updateCandidacy(Long id, Candidacy candidacy);

    void deleteCandidacy(Long id);

    void acceptApplication(Long id);

    void declineApplication(Long id);
}
