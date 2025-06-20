package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Admin;
import com.app.recruitmentapp.entities.Recruiter;

import java.util.List;
import java.util.Optional;

public interface AdminService {

    List<Admin> getAllAdmins();

    Optional<Admin> getAdminById(Long id);

    // Admin saveAdmin(Admin admin);
    Admin registerAdmin(String email, String rawPassword);


    Admin updateAdmin(Long id, Admin admin);

    void deleteAdmin(Long id);

    void activateRecruiterAccount(Long recruiterId);

    void desactivateRecruiterAccount(Long recruiterId);

    void activateCandidateAccount(Long candidateId);

    void desactivateCandidateAccount(Long candidateId);




}
