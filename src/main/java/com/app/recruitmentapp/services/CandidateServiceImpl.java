package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.Role;
import com.app.recruitmentapp.exceptions.EmailAlreadyUsedException;
import com.app.recruitmentapp.repositories.CandidateRepository;
import com.app.recruitmentapp.repositories.UserRepository;
import com.app.recruitmentapp.security.JwtUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class CandidateServiceImpl implements CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private UserRepository userRepository;

    private static final String CV_FOLDER = "/C:/downloads";
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    @Override
    public Optional<Candidate> getCandidateById(Long id) {
        return candidateRepository.findById(id);
    }

    @Override
    public Candidate saveCandidateWithPicture(Candidate candidate, MultipartFile imageFile) {
        candidate.setActive(false);
        candidate.setRole(Role.CANDIDATE);
        if (imageFile != null && !imageFile.isEmpty()) {
            String ext = "." + FilenameUtils.getExtension(imageFile.getOriginalFilename());
            String fileName = UUID.randomUUID().toString();
            String finalFileName = fileName + ext;

            try {
                Path rootLocation = Paths.get(uploadDir);
                System.out.println(rootLocation.toAbsolutePath());
                Files.copy(imageFile.getInputStream(), rootLocation.resolve(finalFileName));

                candidate.setImage(finalFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return candidateRepository.save(candidate);
    }

    @Override
    public Candidate registerCandidate(String email, String rawPassword) {

        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyUsedException("Cet e-mail est déjà utilisé");
        }

        Candidate candidate = new Candidate();
        candidate.setActive(false);
        candidate.setRole(Role.CANDIDATE);
        candidate.setEmail(email);
        candidate.setPassword(passwordEncoder.encode(rawPassword));

        return candidateRepository.save(candidate);
    }
   /*
   @Override
    public Candidate saveCandidateWithoutPicture(Candidate candidate) {
        candidate.setActive(false);
        candidate.setRole(Role.CANDIDATE);
        String hashedPassword = passwordEncoder.encode(candidate.getPassword());
        candidate.setPassword(hashedPassword);
        return candidateRepository.save(candidate);
    }

    */

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
            System.out.println("Bien");
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

   /*@Override
    public Candidate login(String email, String rawPassword) {
        Candidate candidate = candidateRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        if (!passwordEncoder.matches(rawPassword, candidate.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect");
        }
        return candidate;
    }

    */

    @Override
    public ResponseEntity<Resource> getFile(String filename) {
        // Il construit le chemin complet du fichier en combinant le dossier d’upload //(`uploadDir`) et le nom du fichier reçu.
        Path file = Paths.get(uploadDir).resolve(filename);
        Resource resource;
        try {
//Il convertit le fichier en un objet `Resource` pour pouvoir être renvoyé dans la réponse HTTP
            resource = new UrlResource(file.toUri());
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        }
        if (resource.exists() || resource.isReadable()) {
//HttpHeaders.CONTENT_DISPOSITION permet d'envoyer le fichier avec une suggestion de téléchargement.
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() +"\"").body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
