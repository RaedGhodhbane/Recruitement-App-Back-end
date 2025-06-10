package com.app.recruitmentapp.repositories;

import com.app.recruitmentapp.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question,Long> {
}
