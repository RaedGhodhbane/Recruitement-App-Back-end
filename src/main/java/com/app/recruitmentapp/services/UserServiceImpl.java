package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.UserDTO;
import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.Recruiter;
import com.app.recruitmentapp.entities.User;
import com.app.recruitmentapp.exceptions.AuthenticationFailedException;
import com.app.recruitmentapp.exceptions.ResourceNotFoundException;
import com.app.recruitmentapp.mapper.EntityMapper;
import com.app.recruitmentapp.repositories.UserRepository;
import com.app.recruitmentapp.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
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

    @Autowired
    private EntityMapper entityMapper;

    @Override
    public List<UserDTO> getAllUsers() {
        return entityMapper.toUserDTOList(userRepository.findAll());
    }

    @Override
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(entityMapper::toUserDTO);
    }

    @Override
    public UserDTO saveUser(UserDTO userDTO, String password) {
        User user = entityMapper.toUserEntity(userDTO);
        user.setPassword(passwordEncoder.encode(password));
        return entityMapper.toUserDTO(userRepository.save(user));
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        if (userDTO.getName() != null)
            u.setName(userDTO.getName());

        if (userDTO.getFirstName() != null)
            u.setFirstName(userDTO.getFirstName());

        if (userDTO.getEmail() != null)
            u.setEmail(userDTO.getEmail());

        if (userDTO.getRole() != null)
            u.setRole(com.app.recruitmentapp.entities.Role.valueOf(userDTO.getRole()));

        userRepository.saveAndFlush(u);
        return entityMapper.toUserDTO(u);
    }


    @Override
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Utilisateur non trouvé");
        }
    }

    public Map<String, Object> login(String email, String rawPassword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationFailedException("Utilisateur non trouvé"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new AuthenticationFailedException("Mot de passe incorrect");
        }

        if (user instanceof Candidate candidate) {
            if (!candidate.isActive()) {
                throw new AuthenticationFailedException("Compte désactivé. Veuillez contacter l'administrateur.");
            }
        }

        if (user instanceof Recruiter recruiter) {
            if (!recruiter.isActive()) {
                throw new AuthenticationFailedException("Compte désactivé. Veuillez contacter l'administrateur.");
            }
        }

        List<String> roles = List.of("ROLE_" + user.getRole().name());


        String token = jwtUtil.generateToken(user.getEmail(), roles);


        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", entityMapper.toUserDTO(user));

        return response;
    }

    @Override
    public void logout(String token) {
        log.info("Logout - JWT token reçu : {}", token);
        if (token != null) {
            Date expiration = jwtUtil.extractExpiration(token);
            LocalDateTime expirationTime = expiration.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            tokenBlacklistService.blacklistToken(token, expirationTime);
        }
    }
}
