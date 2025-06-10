package com.app.recruitmentapp.repositories;

import com.app.recruitmentapp.entities.Candidacy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidacyRepository extends JpaRepository<Candidacy, Long> {
}
