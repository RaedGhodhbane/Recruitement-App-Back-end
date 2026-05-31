package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.security.JwtUtil;
import com.app.recruitmentapp.services.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Authentification", description = "Endpoints de connexion et déconnexion")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "Authentifier un utilisateur", description = "Connecte un utilisateur et retourne un token JWT")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        Map<String, Object> result = userService.login(email, password);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Déconnecter un utilisateur", description = "Invalide le token JWT de l'utilisateur")
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
        String token = jwtUtil.extractToken(request);
        if (token != null) {
            userService.logout(token);
            return ResponseEntity.ok(Map.of("message", "Déconnexion réussie"));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "Token manquant"));
    }

}

