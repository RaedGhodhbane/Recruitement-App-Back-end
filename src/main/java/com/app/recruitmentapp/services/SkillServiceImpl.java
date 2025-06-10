package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.Skill;
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
    @Override
    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    @Override
    public Optional<Skill> getSkillById(Long id) {
        return skillRepository.findById(id);
    }

    @Override
    public Skill saveSkill(Skill skill, Long idCandidate) {
        Candidate c = candidateRepository.findById(idCandidate).orElse(null);
        skill.setCandidate(c);
        return skillRepository.save(skill);
    }

    @Override
    public Skill updateSkill(Long id, Skill newSkill) {
        Skill sk = skillRepository.findById(id).orElse(null);
        sk.setTitle(newSkill.getTitle());
        sk.setLevel(newSkill.getLevel());
        sk.setCandidate(newSkill.getCandidate());
        skillRepository.saveAndFlush(sk);
        return sk;
    }

    @Override
    public void deleteSkill(Long id) {
        if (skillRepository.existsById(id)) {
            skillRepository.deleteById(id);
        } else {
            throw new RuntimeException("Recruteur non trouvé");
        }
    }
}
