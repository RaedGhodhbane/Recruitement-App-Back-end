package com.app.recruitmentapp.repositories;

import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

}
