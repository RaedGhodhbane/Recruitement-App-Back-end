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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

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

        String token = jwtUtil.generateToken(user.getEmail());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", user);

        return response;
    }


}
