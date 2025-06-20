package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Admin;
import com.app.recruitmentapp.entities.Recruiter;
import com.app.recruitmentapp.entities.Role;
import com.app.recruitmentapp.exceptions.EmailAlreadyUsedException;
import com.app.recruitmentapp.repositories.AdminRepository;
import com.app.recruitmentapp.repositories.CandidateRepository;
import com.app.recruitmentapp.repositories.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public Admin registerAdmin(String email, String rawPassword) {

        if (adminRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyUsedException("Cet e-mail est déjà utilisé");
        }

        Admin admin = new Admin();
        admin.setActive(false);
        admin.setRole(Role.ADMIN);
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(rawPassword));

        return adminRepository.save(admin);
    }

    @Override
    public Admin updateAdmin(Long id, Admin newAdmin) {
        Admin a = adminRepository.findById(id).orElse(null);
        a.setName(newAdmin.getName());
        a.setFirstName(newAdmin.getFirstName());
        a.setEmail(newAdmin.getEmail());
        a.setPassword(newAdmin.getPassword());
        a.setRole(newAdmin.getRole());
        a.setActive(newAdmin.getActive());
        adminRepository.saveAndFlush(a);
        return a;
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
}
