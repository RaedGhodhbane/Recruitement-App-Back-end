package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.CandidateDTO;
import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.ChangePassword;
import com.app.recruitmentapp.entities.Role;
import com.app.recruitmentapp.exceptions.EmailAlreadyUsedException;
import com.app.recruitmentapp.exceptions.ResourceNotFoundException;
import com.app.recruitmentapp.mapper.EntityMapper;
import com.app.recruitmentapp.repositories.CandidateRepository;
import com.app.recruitmentapp.repositories.UserRepository;
import com.app.recruitmentapp.security.JwtUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Slf4j
@Service
public class CandidateServiceImpl implements CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EntityMapper entityMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private UserRepository userRepository;

    @Value("${cv.upload.dir}")
    private String cvUploadDir;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public List<CandidateDTO> getAllCandidates() {
        return entityMapper.toCandidateDTOList(candidateRepository.findAll());
    }

    @Override
    public Optional<CandidateDTO> getCandidateById(Long id) {
        return candidateRepository.findById(id).map(entityMapper::toCandidateDTO);
    }

    @Override
    public CandidateDTO saveCandidateWithPicture(CandidateDTO candidateDTO, MultipartFile imageFile) {
        Candidate candidate = entityMapper.toCandidateEntity(candidateDTO);
        candidate.setActive(false);
        candidate.setRole(Role.CANDIDATE);
        if (imageFile != null && !imageFile.isEmpty()) {
            String ext = "." + FilenameUtils.getExtension(imageFile.getOriginalFilename());
            String fileName = UUID.randomUUID().toString();
            String finalFileName = fileName + ext;

            try {
                Path rootLocation = Paths.get(uploadDir);
                log.debug("Root location: {}", rootLocation.toAbsolutePath());
                Files.copy(imageFile.getInputStream(), rootLocation.resolve(finalFileName));

                candidate.setImage(finalFileName);
            } catch (IOException e) {
                log.error("Error copying file", e);
            }
        }
        return entityMapper.toCandidateDTO(candidateRepository.save(candidate));
    }

    @Override
    public CandidateDTO registerCandidate(String email, String rawPassword
            , String name, String firstName, String phone) {

        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyUsedException("Cet e-mail est déjà utilisé");
        }

        Candidate candidate = new Candidate();
        candidate.setActive(false);
        candidate.setRole(Role.CANDIDATE);
        candidate.setEmail(email);
        candidate.setPassword(passwordEncoder.encode(rawPassword));
        candidate.setName(name);
        candidate.setFirstName(firstName);
        candidate.setPhone(phone);

        return entityMapper.toCandidateDTO(candidateRepository.save(candidate));
    }

    @Override
    public CandidateDTO updateCandidate(Long id, CandidateDTO candidateDTO) {
        Candidate c = candidateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

        c.setName(candidateDTO.getName());
        c.setFirstName(candidateDTO.getFirstName());
        c.setEmail(candidateDTO.getEmail());
        if (candidateDTO.getRole() != null) {
            c.setRole(Role.valueOf(candidateDTO.getRole()));
        }
        c.setCv(candidateDTO.getCv());
        c.setDescription(candidateDTO.getDescription());
        c.setAddress(candidateDTO.getAddress());
        c.setTitle(candidateDTO.getTitle());
        c.setActive(candidateDTO.isActive());
        c.setPhone(candidateDTO.getPhone());
        c.setDateOfBirth(candidateDTO.getDateOfBirth());
        c.setGender(candidateDTO.getGender());
        candidateRepository.saveAndFlush(c);

        return entityMapper.toCandidateDTO(c);
    }

    @Override
    public void deleteCandidate(Long id) {
        if (candidateRepository.existsById(id)) {
            log.info("Candidate deleted: {}", id);
            candidateRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Candidat non trouvé");
        }
    }

    @Override
    public void createCV(Long candidateId, MultipartFile cvFile) throws IOException {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

        String filename = "cv_" + candidateId + ".pdf";

        Path dest = Paths.get(cvUploadDir, filename);
        Files.createDirectories(dest.getParent());
        Files.copy(cvFile.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);

        candidate.setCvPath(dest.toAbsolutePath().toString());
        candidateRepository.save(candidate);
    }

    @Override
    public byte[] downloadCVPDF(Long candidateId) throws IOException {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

        if (candidate.getCvPath() == null) {
            throw new ResourceNotFoundException("CV not found for candidate");
        }

        Path path = Paths.get(candidate.getCvPath());
        return Files.readAllBytes(path);
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
            return ResponseEntity.notFound().build();
        }
        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() +"\"").body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @Override
    public String changePassword(Long candidateId, ChangePassword changePasswordRequest) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidat non trouvé"));

        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), candidate.getPassword())) {
            throw new IllegalArgumentException("Ancien mot de passe incorrect !");
        }

        candidate.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        candidateRepository.save(candidate);

        return "Mot de passe changé avec succès !";
    }
}
