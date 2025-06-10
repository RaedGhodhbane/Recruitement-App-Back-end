package com.app.recruitmentapp.repositories;

import com.app.recruitmentapp.entities.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill,Long> {
}
