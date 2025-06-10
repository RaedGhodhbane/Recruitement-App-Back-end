package com.app.recruitmentapp.repositories;

import com.app.recruitmentapp.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface AdminRepository extends JpaRepository<Admin,Long> {
}
