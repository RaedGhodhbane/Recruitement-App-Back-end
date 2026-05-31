package com.app.recruitmentapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserDTO {
    private Long id;
    @NotBlank(message = "Le nom est requis")
    private String name;
    @NotBlank(message = "Le prénom est requis")
    private String firstName;
    @NotBlank(message = "L'email est requis")
    @Email(message = "L'email doit être valide")
    private String email;
    @NotBlank(message = "Le rôle est requis")
    private String role;

    public UserDTO() {}

    public UserDTO(Long id, String name, String firstName, String email, String role) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
        this.email = email;
        this.role = role;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
