package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.RecruiterDTO;
import com.app.recruitmentapp.entities.Recruiter;
import com.app.recruitmentapp.entities.Role;
import com.app.recruitmentapp.exceptions.EmailAlreadyUsedException;
import com.app.recruitmentapp.exceptions.ResourceNotFoundException;
import com.app.recruitmentapp.mapper.EntityMapper;
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

    @Autowired
    private EntityMapper entityMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public List<RecruiterDTO> getAllRecruiters() {
        return entityMapper.toRecruiterDTOList(recruiterRepository.findAll());
    }

    @Override
    public Optional<RecruiterDTO> getRecruiterById(Long id) {
        return recruiterRepository.findById(id).map(entityMapper::toRecruiterDTO);
    }

    @Override
    public RecruiterDTO registerRecruiter(String email, String rawPassword
            , String name, String firstName, String phone) {

        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyUsedException("Cet e-mail est déjà utilisé");
        }

        Recruiter recruiter = new Recruiter();
        recruiter.setActive(false);
        recruiter.setRole(Role.RECRUITER);
        recruiter.setEmail(email);
        recruiter.setPassword(passwordEncoder.encode(rawPassword));
        recruiter.setName(name);
        recruiter.setFirstName(firstName);
        recruiter.setPhone(phone);

        return entityMapper.toRecruiterDTO(recruiterRepository.save(recruiter));
    }

    @Override
    public RecruiterDTO addRecruiterWithPicture(RecruiterDTO recruiterDTO, MultipartFile imageFile) {
        Recruiter recruiter = entityMapper.toRecruiterEntity(recruiterDTO);
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
        return entityMapper.toRecruiterDTO(recruiterRepository.save(recruiter));
    }


    @Override
    public RecruiterDTO updateRecruiter(Long id, RecruiterDTO recruiterDTO) {
         Recruiter r = recruiterRepository.findById(id)
                 .orElseThrow(() -> new ResourceNotFoundException("Recruteur non trouvé"));
        if (recruiterDTO.getName() != null) {
            r.setName(recruiterDTO.getName());
        }

        if (recruiterDTO.getFirstName() != null) {
            r.setFirstName(recruiterDTO.getFirstName());
        }

        if (recruiterDTO.getAddress() != null) {
            r.setAddress(recruiterDTO.getAddress());
        }

        if (recruiterDTO.getEmail() != null) {
            r.setEmail(recruiterDTO.getEmail());
        }

        if (recruiterDTO.getRole() != null) {
            r.setRole(Role.valueOf(recruiterDTO.getRole()));
        }

        if (recruiterDTO.getCreationDate() != null) {
            r.setCreationDate(recruiterDTO.getCreationDate());
        }

        if (recruiterDTO.getCompanyName() != null) {
            r.setCompanyName(recruiterDTO.getCompanyName());
        }

        if (recruiterDTO.getPhone() != null) {
            r.setPhone(recruiterDTO.getPhone());
        }

        if (recruiterDTO.getWebsite() != null) {
            r.setWebsite(recruiterDTO.getWebsite());
        }

        if (recruiterDTO.getDescription() != null) {
            r.setDescription(recruiterDTO.getDescription());
        }
         recruiterRepository.saveAndFlush(r);
         return entityMapper.toRecruiterDTO(r);
    }
    @Override
    public void deleteRecruiter(Long id) {
        if (recruiterRepository.existsById(id)) {
            recruiterRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Recruteur non trouvé");
        }
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
}
