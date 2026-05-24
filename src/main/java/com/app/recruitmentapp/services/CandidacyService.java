package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.CandidacyDTO;

import java.util.List;
import java.util.Optional;

public interface CandidacyService {

    List<CandidacyDTO> getAllCandidacies();

    Optional<CandidacyDTO> getCandidacyById(Long id);

    CandidacyDTO saveCandidacy(CandidacyDTO candidacyDTO);

    CandidacyDTO updateCandidacy(Long id, CandidacyDTO candidacyDTO);

    void deleteCandidacy(Long id);

    void acceptApplication(Long id);

    void declineApplication(Long id);

    boolean candidacyExists(Long offerId, Long candidateId);
}
