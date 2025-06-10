package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.User;
import com.app.recruitmentapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
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


}
