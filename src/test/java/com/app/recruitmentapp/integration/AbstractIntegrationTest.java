package com.app.recruitmentapp.integration;

import com.app.recruitmentapp.entities.*;
import com.app.recruitmentapp.repositories.*;
import com.app.recruitmentapp.security.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected JwtUtil jwtUtil;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected CandidateRepository candidateRepository;

    @Autowired
    protected RecruiterRepository recruiterRepository;

    @Autowired
    protected OfferRepository offerRepository;

    @Autowired
    protected CandidacyRepository candidacyRepository;

    @Autowired
    protected SkillRepository skillRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected AdminRepository adminRepository;

    @Autowired
    protected ContactRepository contactRepository;

    @Autowired
    protected EducationRepository educationRepository;

    @Autowired
    protected ExperienceRepository experienceRepository;

    @Autowired
    protected FavouriteRepository favouriteRepository;

    @Autowired
    protected MessageRepository messageRepository;

    @Autowired
    protected QuestionRepository questionRepository;

    @AfterEach
    void cleanDatabase() {
        favouriteRepository.deleteAllInBatch();
        messageRepository.deleteAllInBatch();
        contactRepository.deleteAllInBatch();
        questionRepository.deleteAllInBatch();
        candidacyRepository.deleteAllInBatch();
        educationRepository.deleteAllInBatch();
        experienceRepository.deleteAllInBatch();
        skillRepository.deleteAllInBatch();
        offerRepository.deleteAllInBatch();
        candidateRepository.deleteAllInBatch();
        recruiterRepository.deleteAllInBatch();
        adminRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    protected String generateToken(String email, String role) {
        return jwtUtil.generateToken(email, List.of("ROLE_" + role));
    }

    protected org.springframework.http.HttpEntity<Void> authHeader(String token) {
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setBearerAuth(token);
        return new org.springframework.http.HttpEntity<>(headers);
    }

    protected <T> org.springframework.http.HttpEntity<T> authEntity(T body, String token) {
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setBearerAuth(token);
        return new org.springframework.http.HttpEntity<>(body, headers);
    }

    protected Candidate createCandidate(String email) {
        Candidate c = new Candidate();
        c.setEmail(email);
        c.setPassword(passwordEncoder.encode("password123"));
        c.setName("Test");
        c.setFirstName("User");
        c.setPhone("0612345678");
        c.setRole(Role.CANDIDATE);
        c.setActive(true);
        return candidateRepository.saveAndFlush(c);
    }

    protected Admin createAdmin(String email) {
        Admin a = new Admin();
        a.setEmail(email);
        a.setPassword(passwordEncoder.encode("password123"));
        a.setName("Admin");
        a.setFirstName("User");
        a.setPhone("0612345678");
        a.setRole(Role.ADMIN);
        a.setActive(true);
        return adminRepository.saveAndFlush(a);
    }

    protected Recruiter createRecruiter(String email) {
        Recruiter r = new Recruiter();
        r.setEmail(email);
        r.setPassword(passwordEncoder.encode("password123"));
        r.setName("Test");
        r.setFirstName("Recruiter");
        r.setPhone("0612345678");
        r.setRole(Role.RECRUITER);
        r.setActive(true);
        return recruiterRepository.saveAndFlush(r);
    }
}
