package com.app.recruitmentapp.repositories;

import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RecruiterRepository extends JpaRepository<Recruiter,Long> {
    Optional<Recruiter> findByEmail(String email);

}
