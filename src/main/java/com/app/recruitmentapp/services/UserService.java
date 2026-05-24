package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.UserDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {

    List<UserDTO> getAllUsers();

    Optional<UserDTO> getUserById(Long id);

    UserDTO saveUser(UserDTO userDTO, String password);

    UserDTO updateUser(Long id, UserDTO userDTO);

    void deleteUser(Long id);

    Map<String, Object> login(String email, String rawPassword);

    void logout(String token);
}
