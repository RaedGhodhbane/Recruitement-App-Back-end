package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.Recruiter;
import com.app.recruitmentapp.entities.Role;
import com.app.recruitmentapp.exceptions.EmailAlreadyUsedException;
import com.app.recruitmentapp.repositories.RecruiterRepository;
import com.app.recruitmentapp.repositories.UserRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecruiterServiceImpl implements RecruiterService {

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public List<Recruiter> getAllRecruiters() {
        return recruiterRepository.findAll();
    }

    @Override
    public Optional<Recruiter> getRecruiterById(Long id) {
        return recruiterRepository.findById(id);
    }

    /*@Override
    public Recruiter addRecruiterWithoutPicture(Recruiter recruiter) {
        recruiter.setActive(false);
        recruiter.setRole(Role.RECRUITER);
        return recruiterRepository.save(recruiter);
    }
     */

    @Override
    public Recruiter registerRecruiter(String email, String rawPassword) {

        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyUsedException("Cet e-mail est déjà utilisé");
        }

        Recruiter recruiter = new Recruiter();
        recruiter.setActive(false);
        recruiter.setRole(Role.RECRUITER);
        recruiter.setEmail(email);
        recruiter.setPassword(passwordEncoder.encode(rawPassword));

        return recruiterRepository.save(recruiter);
    }

    @Override
    public Recruiter addRecruiterWithPicture(Recruiter recruiter, MultipartFile imageFile) {
        recruiter.setActive(false);
        recruiter.setRole(Role.RECRUITER);
        if (imageFile != null && !imageFile.isEmpty()) {
            String ext = "." + FilenameUtils.getExtension(imageFile.getOriginalFilename());
            String fileName = UUID.randomUUID().toString();
            String finalFileName = fileName + ext;

            try {
                Path rootLocation = Paths.get(uploadDir);
                Files.copy(imageFile.getInputStream(), rootLocation.resolve(finalFileName));
                recruiter.setImage(finalFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return recruiterRepository.save(recruiter);
    }


    @Override
    public Recruiter updateRecruiter(Long id, Recruiter newRecruiter) {
         Recruiter r = recruiterRepository.findById(id).orElse(null);
         r.setName(newRecruiter.getName());
         r.setFirstName(newRecruiter.getFirstName());
         r.setActive(newRecruiter.getActive());
         r.setAddress(newRecruiter.getAddress());
         r.setEmail(newRecruiter.getEmail());
         r.setPassword(newRecruiter.getPassword());
         r.setRole(newRecruiter.getRole());
         r.setCreationDate(newRecruiter.getCreationDate());
         r.setOfferList(newRecruiter.getOfferList());
         recruiterRepository.saveAndFlush(r);
         return r;
    }
    @Override
    public void deleteRecruiter(Long id) {
        if (recruiterRepository.existsById(id)) {
            recruiterRepository.deleteById(id);
        } else {
            throw new RuntimeException("Recruteur non trouvé");
        }
    }

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
