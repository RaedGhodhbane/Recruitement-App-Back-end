package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.Recruiter;
import com.app.recruitmentapp.entities.Role;
import com.app.recruitmentapp.entities.User;
import com.app.recruitmentapp.repositories.UserRepository;
import com.app.recruitmentapp.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    private Role role;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User newUser) {
        User u = userRepository.findById(id).orElse(null);
        if (u == null) return null;

        if (newUser.getName() != null)
            u.setName(newUser.getName());

        if (newUser.getFirstName() != null)
            u.setFirstName(newUser.getFirstName());

        if (newUser.getEmail() != null)
            u.setEmail(newUser.getEmail());

        if (newUser.getPassword() != null && !newUser.getPassword().isBlank())
            u.setPassword(newUser.getPassword());

        if (newUser.getRole() != null)
            u.setRole(newUser.getRole());

        userRepository.saveAndFlush(u);
        return u;
    }


    @Override
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("Recruteur non trouvé");
        }
    }

    public Map<String, Object> login(String email, String rawPassword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        if (user instanceof Candidate candidate) {
            if (!candidate.isActive()) {
                throw new RuntimeException("Compte désactivé. Veuillez contacter l'administrateur.");
            }
        }

        if (user instanceof Recruiter recruiter) {
            if (!recruiter.isActive()) {
                throw new RuntimeException("Compte désactivé. Veuillez contacter l'administrateur.");
            }
        }

        List<String> roles = List.of("ROLE_" + user.getRole().name());


        String token = jwtUtil.generateToken(user.getEmail(), roles);


        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", user);

        return response;
    }

    @Override
    public void logout(String token) {
        System.out.println("Logout - JWT token reçu : " + token);
        if (token != null) {
            Date expiration = jwtUtil.extractExpiration(token);
            LocalDateTime expirationTime = expiration.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            tokenBlacklistService.blacklistToken(token, expirationTime);
        }
    }

}
