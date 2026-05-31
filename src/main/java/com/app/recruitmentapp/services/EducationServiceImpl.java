package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.EducationDTO;
import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.Education;
import com.app.recruitmentapp.mapper.EntityMapper;
import com.app.recruitmentapp.exceptions.ResourceNotFoundException;
import com.app.recruitmentapp.repositories.CandidateRepository;
import com.app.recruitmentapp.repositories.EducationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EducationServiceImpl implements EducationService {
    @Autowired
    private EducationRepository educationRepository;
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public List<EducationDTO> getAllEducations() {
        return entityMapper.toEducationDTOList(educationRepository.findAll());
    }

    @Override
    public Optional<EducationDTO> getEducationById(Long id) {
        return educationRepository.findById(id).map(entityMapper::toEducationDTO);
    }

    @Override
    public EducationDTO saveEducation(EducationDTO educationDTO, Long idCandidate) {
        Education education = entityMapper.toEducationEntity(educationDTO);
        Candidate c = candidateRepository.findById(idCandidate).orElse(null);
        education.setCandidate(c);
        return entityMapper.toEducationDTO(educationRepository.save(education));
    }

    @Override
    public EducationDTO updateEducation(Long id, EducationDTO educationDTO) {
        Education e = educationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Education non trouvé"));
        e.setDiploma(educationDTO.getDiploma());
        e.setUniversity(educationDTO.getUniversity());
        e.setEndDate(educationDTO.getEndDate());
        e.setDescription(educationDTO.getDescription());
        educationRepository.saveAndFlush(e);
        return entityMapper.toEducationDTO(e);
    }

    @Override
    public void deleteEducation(Long id) {
        if (educationRepository.existsById(id)) {
            educationRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Education non trouvé");
        }
    }
}
