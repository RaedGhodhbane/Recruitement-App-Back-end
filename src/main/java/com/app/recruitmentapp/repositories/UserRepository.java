package com.app.recruitmentapp.repositories;

import com.app.recruitmentapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
