package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Admin;
import com.app.recruitmentapp.entities.ChangePassword;
import com.app.recruitmentapp.entities.Recruiter;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface AdminService {

    List<Admin> getAllAdmins();

    Optional<Admin> getAdminById(Long id);

    // Admin saveAdmin(Admin admin);
    Admin registerAdminWithPicture(String email, String rawPassword, Admin admin, MultipartFile imageFile);


    Admin updateAdmin(Long id, Admin admin, MultipartFile imageFile);

    void deleteAdmin(Long id);

    void activateRecruiterAccount(Long recruiterId);

    void desactivateRecruiterAccount(Long recruiterId);

    void activateCandidateAccount(Long candidateId);

    void desactivateCandidateAccount(Long candidateId);

    String changePassword(Long id, ChangePassword changePasswordRequest);

    ResponseEntity<Resource> getFile(String filename);






}
