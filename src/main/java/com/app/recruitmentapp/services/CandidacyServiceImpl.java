package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.CandidacyDTO;
import com.app.recruitmentapp.entities.Candidacy;
import com.app.recruitmentapp.entities.Status;
import com.app.recruitmentapp.mapper.EntityMapper;
import com.app.recruitmentapp.repositories.CandidacyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidacyServiceImpl implements CandidacyService {

    private final CandidacyRepository candidacyRepository;
    private final EntityMapper entityMapper;

    public CandidacyServiceImpl(CandidacyRepository candidacyRepository, EntityMapper entityMapper) {
        this.candidacyRepository = candidacyRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    public List<CandidacyDTO> getAllCandidacies() {
        return entityMapper.toCandidacyDTOList(candidacyRepository.findAll());
    }

    @Override
    public Optional<CandidacyDTO> getCandidacyById(Long id) {
        return candidacyRepository.findById(id).map(entityMapper::toCandidacyDTO);
    }

    @Override
    public CandidacyDTO saveCandidacy(CandidacyDTO candidacyDTO) {
        Candidacy candidacy = entityMapper.toCandidacyEntity(candidacyDTO);
        return entityMapper.toCandidacyDTO(candidacyRepository.save(candidacy));
    }

    @Override
    public CandidacyDTO updateCandidacy(Long id, CandidacyDTO candidacyDTO) {
        Candidacy c = candidacyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Candidacy not found with id " + id));
        c.setSubmissionDate(candidacyDTO.getSubmissionDate());
        if (candidacyDTO.getStatus() != null) c.setStatus(Status.valueOf(candidacyDTO.getStatus()));
        c.setScore(candidacyDTO.getScore());
        return entityMapper.toCandidacyDTO(candidacyRepository.save(c));
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
