package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.*;
import com.app.recruitmentapp.exceptions.EmailAlreadyUsedException;
import com.app.recruitmentapp.repositories.AdminRepository;
import com.app.recruitmentapp.repositories.CandidateRepository;
import com.app.recruitmentapp.repositories.RecruiterRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.io.File;
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
public class AdminServiceImpl implements AdminService{
    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }
    @Override
    public Optional<Admin> getAdminById(Long id) {
        return adminRepository.findById(id);
    }

    /*
    @Override
    public Admin saveAdmin(Admin admin) {
        admin.setActive(true);
        admin.setRole(Role.ADMIN);
        return adminRepository.save(admin);
    }
     */
    @Override
    public Admin registerAdminWithPicture(String email, String rawPassword, Admin admin, MultipartFile imageFile) {

        if (adminRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyUsedException("Cet e-mail est déjà utilisé");
        }

        Admin admin1 = new Admin();
        admin1.setActive(false);
        admin1.setRole(Role.ADMIN);
        admin1.setEmail(email);
        admin1.setPassword(passwordEncoder.encode(rawPassword));

        if (imageFile != null && !imageFile.isEmpty()) {
            String ext = "." + FilenameUtils.getExtension(imageFile.getOriginalFilename());
            String fileName = UUID.randomUUID().toString();
            String finalFileName = fileName + ext;

            try {
                Path rootLocation = Paths.get(uploadDir);
                System.out.println(rootLocation.toAbsolutePath());
                Files.copy(imageFile.getInputStream(), rootLocation.resolve(finalFileName));

                admin1.setImage(finalFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return adminRepository.save(admin1);
    }

    @Override
    public Admin updateAdmin(Long id, Admin newAdmin, MultipartFile imageFile) {
        Admin a = adminRepository.findById(id).orElseThrow();

        if (newAdmin.getName() != null) {
            a.setName(newAdmin.getName());
        }
        if (newAdmin.getFirstName() != null) {
            a.setFirstName(newAdmin.getFirstName());
        }
        if (newAdmin.getEmail() != null) {
            a.setEmail(newAdmin.getEmail());
        }
        if (newAdmin.getPassword() != null) {
            a.setPassword(newAdmin.getPassword());
        }
        if (newAdmin.getGender() != null) {
            a.setGender(newAdmin.getGender());
        }
        if (newAdmin.getBirthdate() != null) {
            a.setBirthdate(newAdmin.getBirthdate());
        }
        if (newAdmin.getAddress() != null) {
            a.setAddress(newAdmin.getAddress());
        }
        if (newAdmin.getCity() != null) {
            a.setCity(newAdmin.getCity());
        }
        if (newAdmin.getState() != null) {
            a.setState(newAdmin.getState());
        }
        if (newAdmin.getCountry() != null) {
            a.setCountry(newAdmin.getCountry());
        }
        if (newAdmin.getPhone() != null) {
            a.setPhone(newAdmin.getPhone());
        }

        if(imageFile != null && !imageFile.isEmpty()) {
            String fileName = saveFile(imageFile);
            a.setImage(fileName);
        }

        return adminRepository.saveAndFlush(a);
    }


    public String saveFile(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            Path filePath = Paths.get(uploadDir, fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement du fichier", e);
        }
    }

    @Override
    public void deleteAdmin(Long id) {
        if (adminRepository.existsById(id)) {
            adminRepository.deleteById(id);
        } else {
            throw new RuntimeException("Recruteur non trouvé");
        }
    }

    @Override
    @Transactional
    public void activateRecruiterAccount(Long recruiterId) {
        recruiterRepository.findById(recruiterId).ifPresentOrElse(recruiter -> {
            recruiter.setActive(true);
            recruiterRepository.save(recruiter);
        }, () -> {
            throw new RuntimeException("Recruteur non trouvé avec l'ID: " + recruiterId);
        });
    }

    @Override
    @Transactional
    public void desactivateRecruiterAccount(Long recruiterId) {
        recruiterRepository.findById(recruiterId).ifPresentOrElse(recruiter -> {
            recruiter.setActive(false);
            recruiterRepository.save(recruiter);
        }, () -> {
            throw new RuntimeException("Recruteur non trouvé avec l'ID: " + recruiterId);
        });
    }

    @Override
    @Transactional
    public void activateCandidateAccount(Long candidateId) {
        candidateRepository.findById(candidateId).ifPresentOrElse(candidate -> {
            candidate.setActive(true);
            candidateRepository.save(candidate);
        }, () -> {
            throw new RuntimeException("Candidat non trouvé avec l'ID: " + candidateId);
        });
    }

    @Override
    @Transactional
    public void desactivateCandidateAccount(Long candidateId) {
        candidateRepository.findById(candidateId).ifPresentOrElse(candidate -> {
            candidate.setActive(false);
            candidateRepository.save(candidate);
        }, () -> {
            throw new RuntimeException("Candidat non trouvé avec l'ID: " + candidateId);
        });
    }

    @Override
    public String changePassword(Long adminId, ChangePassword changePasswordRequest) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin non trouvé"));

        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), admin.getPassword())) {
            throw new IllegalArgumentException("Ancien mot de passe incorrect !");
        }

        admin.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        adminRepository.save(admin);

        return "Mot de passe changé avec succès !";
    }

    @Override
    public ResponseEntity<Resource> getFile(String filename) {
        Path file = Paths.get(uploadDir).resolve(filename);
        Resource resource;
        try {
            resource = new UrlResource(file.toUri());
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        }
        if (resource.exists() && resource.isReadable()) {
            MediaType mediaType = MediaType.IMAGE_JPEG; // ou détecte à partir du filename
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }

    }
}
