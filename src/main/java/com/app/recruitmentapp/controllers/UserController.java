package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.dto.UserDTO;
import com.app.recruitmentapp.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Utilisateurs", description = "Gestion des utilisateurs (admin)")
@RestController
@RequestMapping("/admin/user")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "Liste des utilisateurs", description = "Retourne tous les utilisateurs")
    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Détail d'un utilisateur", description = "Retourne un utilisateur par son ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Créer un utilisateur", description = "Ajoute un nouvel utilisateur")
    @PostMapping()
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO userDTO, @RequestParam("password") String password) {
        UserDTO saved = userService.saveUser(userDTO, password);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Modifier un utilisateur", description = "Met à jour un utilisateur existant")
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        try {
            UserDTO updated = userService.updateUser(id, userDTO);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Supprimer un utilisateur", description = "Supprime un utilisateur par son ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
