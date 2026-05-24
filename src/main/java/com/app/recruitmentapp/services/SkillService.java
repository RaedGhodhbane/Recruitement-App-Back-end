package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.SkillDTO;

import java.util.List;
import java.util.Optional;

public interface SkillService {
    List<SkillDTO> getAllSkills();

    Optional<SkillDTO> getSkillById(Long id);

    SkillDTO saveSkill(SkillDTO skillDTO, Long idCandidate);

    SkillDTO updateSkill(Long id, SkillDTO skillDTO);

    void deleteSkill(Long id);
}
