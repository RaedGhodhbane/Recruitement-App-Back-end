package com.app.recruitmentapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class ContactDTO {
    private Long id;
    @NotBlank(message = "Le nom est requis")
    private String name;
    @NotBlank(message = "Le sujet est requis")
    private String subject;
    @NotBlank(message = "L'email est requis")
    @Email(message = "L'email doit être valide")
    private String email;
    @Pattern(regexp = "^\\+?[0-9\\s\\-]{8,15}$", message = "Le numéro de téléphone est invalide")
    private String phone;
    @NotBlank(message = "Le message est requis")
    private String message;
    @NotNull(message = "L'utilisateur est requis")
    private Long userSendId;

    public ContactDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Long getUserSendId() { return userSendId; }
    public void setUserSendId(Long userSendId) { this.userSendId = userSendId; }
}
