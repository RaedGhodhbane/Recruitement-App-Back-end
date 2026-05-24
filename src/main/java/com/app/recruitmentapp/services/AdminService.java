package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.AdminDTO;
import com.app.recruitmentapp.entities.ChangePassword;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface AdminService {

    List<AdminDTO> getAllAdmins();

    Optional<AdminDTO> getAdminById(Long id);

    AdminDTO registerAdminWithPicture(String email, String rawPassword, AdminDTO adminDTO, MultipartFile imageFile);

    AdminDTO updateAdmin(Long id, AdminDTO adminDTO, MultipartFile imageFile);

    void deleteAdmin(Long id);

    void activateRecruiterAccount(Long recruiterId);

    void desactivateRecruiterAccount(Long recruiterId);

    void activateCandidateAccount(Long candidateId);

    void desactivateCandidateAccount(Long candidateId);

    String changePassword(Long id, ChangePassword changePasswordRequest);

    ResponseEntity<Resource> getFile(String filename);
}
