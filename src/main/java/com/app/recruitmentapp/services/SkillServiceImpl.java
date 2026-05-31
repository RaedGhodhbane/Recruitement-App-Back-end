package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.SkillDTO;
import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.Skill;
import com.app.recruitmentapp.mapper.EntityMapper;
import com.app.recruitmentapp.exceptions.ResourceNotFoundException;
import com.app.recruitmentapp.repositories.CandidateRepository;
import com.app.recruitmentapp.repositories.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SkillServiceImpl implements SkillService{
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public List<SkillDTO> getAllSkills() {
        return entityMapper.toSkillDTOList(skillRepository.findAll());
    }

    @Override
    public Optional<SkillDTO> getSkillById(Long id) {
        return skillRepository.findById(id).map(entityMapper::toSkillDTO);
    }

    @Override
    public SkillDTO saveSkill(SkillDTO skillDTO, Long idCandidate) {
        Skill skill = entityMapper.toSkillEntity(skillDTO);
        Candidate c = candidateRepository.findById(idCandidate).orElse(null);
        skill.setCandidate(c);
        return entityMapper.toSkillDTO(skillRepository.save(skill));
    }

    @Override
    public SkillDTO updateSkill(Long id, SkillDTO skillDTO) {
        Skill sk = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill non trouvé"));
        sk.setTitle(skillDTO.getTitle());
        sk.setPercentage(skillDTO.getPercentage());
        skillRepository.saveAndFlush(sk);
        return entityMapper.toSkillDTO(sk);
    }

    @Override
    public void deleteSkill(Long id) {
        if (skillRepository.existsById(id)) {
            skillRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Skill non trouvé");
        }
    }
}
