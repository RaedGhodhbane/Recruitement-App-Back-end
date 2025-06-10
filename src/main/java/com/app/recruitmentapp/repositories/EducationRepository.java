package com.app.recruitmentapp.repositories;

import com.app.recruitmentapp.entities.Education;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EducationRepository extends JpaRepository<Education,Long> {
}
