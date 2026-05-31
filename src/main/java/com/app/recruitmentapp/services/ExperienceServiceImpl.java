package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.ExperienceDTO;
import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.Experience;
import com.app.recruitmentapp.mapper.EntityMapper;
import com.app.recruitmentapp.exceptions.ResourceNotFoundException;
import com.app.recruitmentapp.repositories.CandidateRepository;
import com.app.recruitmentapp.repositories.ExperienceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExperienceServiceImpl implements ExperienceService {
    @Autowired
    private ExperienceRepository experienceRepository;
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public List<ExperienceDTO> getAllExperiences() {
        return entityMapper.toExperienceDTOList(experienceRepository.findAll());
    }

    @Override
    public Optional<ExperienceDTO> getExperienceById(Long id) {
        return experienceRepository.findById(id).map(entityMapper::toExperienceDTO);
    }

    @Override
    public ExperienceDTO saveExperience(ExperienceDTO experienceDTO, Long idCandidate) {
        Experience experience = entityMapper.toExperienceEntity(experienceDTO);
        Candidate c = candidateRepository.findById(idCandidate).orElse(null);
        experience.setCandidate(c);
        return entityMapper.toExperienceDTO(experienceRepository.save(experience));
    }

    @Override
    public ExperienceDTO updateExperience(Long id, ExperienceDTO experienceDTO) {
        Experience ex = experienceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience non trouvé"));
        ex.setCompanyName(experienceDTO.getCompanyName());
        ex.setJobTitle(experienceDTO.getJobTitle());
        ex.setStartExpDate(experienceDTO.getStartExpDate());
        ex.setEndExpDate(experienceDTO.getEndExpDate());
        ex.setDescription(experienceDTO.getDescription());
        experienceRepository.saveAndFlush(ex);
        return entityMapper.toExperienceDTO(ex);
    }

    @Override
    public void deleteExperience(Long id) {
        if (experienceRepository.existsById(id)) {
            experienceRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Experience non trouvé");
        }
    }
}
