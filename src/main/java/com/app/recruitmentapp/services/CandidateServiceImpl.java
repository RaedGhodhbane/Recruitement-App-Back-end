package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.Role;
import com.app.recruitmentapp.repositories.CandidateRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CandidateServiceImpl implements CandidateService{

    @Autowired
    private CandidateRepository candidateRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    private static final String CV_FOLDER = "/C:/downloads";
    @Override
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    @Override
    public Optional<Candidate> getCandidateById(Long id) {
        return candidateRepository.findById(id);
    }

    @Override
    public Candidate saveCandidate(Candidate candidate, MultipartFile imageFile) {
        candidate.setActive(false);
        candidate.setRole(Role.CANDIDATE);
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String ext = "." + FilenameUtils.getExtension(imageFile.getOriginalFilename());
                String fileName = UUID.randomUUID().toString();
                String finalFileName = fileName + ext;

                Path rootLocation = Paths.get(uploadDir);
                Files.copy(imageFile.getInputStream(), rootLocation.resolve(finalFileName));

                candidate.setImage(finalFileName);
            } catch (IOException e) {
                return candidate;
            }
        }
        else {
            System.out.println("error");
        }

        return candidateRepository.save(candidate);
    }

    @Override
    public Candidate updateCandidate(Long id, Candidate newCandidate) {
        Candidate c = candidateRepository.findById(id).orElse(null);
        c.setName(newCandidate.getName());
        c.setFirstName(newCandidate.getFirstName());
        c.setEmail(newCandidate.getEmail());
        c.setPassword(newCandidate.getPassword());
        c.setRole(newCandidate.getRole());
        c.setCv(newCandidate.getCv());
        c.setDescription(newCandidate.getDescription());
        c.setAddress(newCandidate.getAddress());
        c.setTitle(newCandidate.getTitle());
        c.setCandidacyList(newCandidate.getCandidacyList());
        c.setExperienceList(newCandidate.getExperienceList());
        c.setActive(newCandidate.getActive());
        candidateRepository.saveAndFlush(c);
        return c;
    }

    @Override
    public void deleteCandidate(Long id) {
        if (candidateRepository.existsById(id)) {
            candidateRepository.deleteById(id);
        } else {
            throw new RuntimeException("Candidat non trouvé");
        }
    }

    @Override
    public void createCV(Long candidateId, MultipartFile cvFile) throws IOException {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        // Construire un nom de fichier unique par exemple
        String filename = "cv_" + candidateId + ".pdf";

        // Sauvegarder le fichier sur disque
        File dest = new File(CV_FOLDER + filename);
        cvFile.transferTo(dest);

        // Enregistrer le chemin dans l'entité
        candidate.setCvPath(dest.getAbsolutePath());
        candidateRepository.save(candidate);
    }

    @Override
    public byte[] downloadCVPDF(Long candidateId) throws IOException {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        if (candidate.getCvPath() == null) {
            throw new RuntimeException("CV not found for candidate");
        }

        Path path = Paths.get(candidate.getCvPath());
        return Files.readAllBytes(path);
    }

}
