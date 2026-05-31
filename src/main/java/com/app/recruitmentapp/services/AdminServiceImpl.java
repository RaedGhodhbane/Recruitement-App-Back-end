package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.AdminDTO;
import com.app.recruitmentapp.entities.*;
import com.app.recruitmentapp.exceptions.EmailAlreadyUsedException;
import com.app.recruitmentapp.exceptions.ResourceNotFoundException;
import com.app.recruitmentapp.mapper.EntityMapper;
import com.app.recruitmentapp.repositories.AdminRepository;
import com.app.recruitmentapp.repositories.CandidateRepository;
import com.app.recruitmentapp.repositories.RecruiterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final RecruiterRepository recruiterRepository;
    private final AdminRepository adminRepository;
    private final CandidateRepository candidateRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityMapper entityMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public List<AdminDTO> getAllAdmins() {
        return entityMapper.toAdminDTOList(adminRepository.findAll());
    }
    @Override
    public Optional<AdminDTO> getAdminById(Long id) {
        return adminRepository.findById(id).map(entityMapper::toAdminDTO);
    }

    @Override
    public AdminDTO registerAdminWithPicture(String email, String rawPassword, AdminDTO adminDTO, MultipartFile imageFile) {
        if (adminRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyUsedException("Cet e-mail est déjà utilisé");
        }

        Admin admin1 = entityMapper.toAdminEntity(adminDTO);
        admin1.setId(null);
        admin1.setActive(false);
        admin1.setRole(Role.ADMIN);
        admin1.setEmail(email);
        admin1.setPassword(passwordEncoder.encode(rawPassword));

        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = saveFile(imageFile);
            admin1.setImage(fileName);
        }

        return entityMapper.toAdminDTO(adminRepository.save(admin1));
    }

    @Override
    public AdminDTO updateAdmin(Long id, AdminDTO adminDTO, MultipartFile imageFile) {
        Admin a = adminRepository.findById(id).orElseThrow();

        if (adminDTO.getName() != null) {
            a.setName(adminDTO.getName());
        }
        if (adminDTO.getFirstName() != null) {
            a.setFirstName(adminDTO.getFirstName());
        }
        if (adminDTO.getEmail() != null) {
            a.setEmail(adminDTO.getEmail());
        }
        if (adminDTO.getGender() != null) {
            a.setGender(adminDTO.getGender());
        }
        if (adminDTO.getBirthdate() != null) {
            a.setBirthdate(adminDTO.getBirthdate());
        }
        if (adminDTO.getAddress() != null) {
            a.setAddress(adminDTO.getAddress());
        }
        if (adminDTO.getCity() != null) {
            a.setCity(adminDTO.getCity());
        }
        if (adminDTO.getState() != null) {
            a.setState(adminDTO.getState());
        }
        if (adminDTO.getCountry() != null) {
            a.setCountry(adminDTO.getCountry());
        }
        if (adminDTO.getPhone() != null) {
            a.setPhone(adminDTO.getPhone());
        }

        if(imageFile != null && !imageFile.isEmpty()) {
            String fileName = saveFile(imageFile);
            a.setImage(fileName);
        }

        return entityMapper.toAdminDTO(adminRepository.saveAndFlush(a));
    }


    private String saveFile(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            Path uploadPath = Paths.get(uploadDir);
            if (Files.notExists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            log.info("Fichier sauvegardé : {}", fileName);
            return fileName;
        } catch (IOException e) {
            log.error("Erreur lors de l'enregistrement du fichier", e);
            throw new RuntimeException("Erreur lors de l'enregistrement du fichier", e);
        }
    }

    @Override
    public void deleteAdmin(Long id) {
        if (adminRepository.existsById(id)) {
            adminRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Admin non trouvé");
        }
    }

    @Override
    @Transactional
    public void activateRecruiterAccount(Long recruiterId) {
        recruiterRepository.findById(recruiterId).ifPresentOrElse(recruiter -> {
            recruiter.setActive(true);
            recruiterRepository.save(recruiter);
        }, () -> {
            throw new ResourceNotFoundException("Recruteur non trouvé avec l'ID: " + recruiterId);
        });
    }

    @Override
    @Transactional
    public void desactivateRecruiterAccount(Long recruiterId) {
        recruiterRepository.findById(recruiterId).ifPresentOrElse(recruiter -> {
            recruiter.setActive(false);
            recruiterRepository.save(recruiter);
        }, () -> {
            throw new ResourceNotFoundException("Recruteur non trouvé avec l'ID: " + recruiterId);
        });
    }

    @Override
    @Transactional
    public void activateCandidateAccount(Long candidateId) {
        candidateRepository.findById(candidateId).ifPresentOrElse(candidate -> {
            candidate.setActive(true);
            candidateRepository.save(candidate);
        }, () -> {
            throw new ResourceNotFoundException("Candidat non trouvé avec l'ID: " + candidateId);
        });
    }

    @Override
    @Transactional
    public void desactivateCandidateAccount(Long candidateId) {
        candidateRepository.findById(candidateId).ifPresentOrElse(candidate -> {
            candidate.setActive(false);
            candidateRepository.save(candidate);
        }, () -> {
            throw new ResourceNotFoundException("Candidat non trouvé avec l'ID: " + candidateId);
        });
    }

    @Override
    public String changePassword(Long adminId, ChangePassword changePasswordRequest) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin non trouvé"));

        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), admin.getPassword())) {
            throw new IllegalArgumentException("Ancien mot de passe incorrect !");
        }

        admin.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        adminRepository.save(admin);

        return "Mot de passe changé avec succès !";
    }

    @Override
    public ResponseEntity<Resource> getFile(String filename) {
        Path baseDir = Paths.get(uploadDir).normalize();
        Path file = baseDir.resolve(filename).normalize();
        if (!file.startsWith(baseDir)) {
            return ResponseEntity.notFound().build();
        }
        Resource resource;
        try {
            resource = new UrlResource(file.toUri());
        } catch (MalformedURLException e) {
            log.error("Erreur lors du chargement du fichier : {}", filename, e);
            return ResponseEntity.notFound().build();
        }
        if (resource.exists() && resource.isReadable()) {
            MediaType mediaType = MediaType.IMAGE_JPEG;
            try {
                String contentType = Files.probeContentType(file);
                if (contentType != null) {
                    mediaType = MediaType.parseMediaType(contentType);
                }
            } catch (IOException e) {
                log.warn("Impossible de détecter le type MIME pour : {}", filename);
            }
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
