package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Skill;

import java.util.List;
import java.util.Optional;

public interface SkillService {
    List<Skill> getAllSkills();

    Optional<Skill> getSkillById(Long id);

    Skill saveSkill(Skill skill, Long idCandidate);

    Skill updateSkill(Long id, Skill skill);

    void deleteSkill(Long id);
}
