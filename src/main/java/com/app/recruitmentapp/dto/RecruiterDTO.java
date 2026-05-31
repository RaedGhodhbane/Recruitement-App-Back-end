package com.app.recruitmentapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.Instant;

public class RecruiterDTO extends UserDTO {
    @NotBlank(message = "L'adresse est requise")
    private String address;
    private Instant creationDate;
    private String image;
    private boolean active;
    @NotBlank(message = "Le nom de l'entreprise est requis")
    private String companyName;
    @Pattern(regexp = "^\\+?[0-9\\s\\-]{8,15}$", message = "Le numéro de téléphone est invalide")
    private String phone;
    private String website;
    @NotBlank(message = "La description est requise")
    private String description;

    public RecruiterDTO() {}

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Instant getCreationDate() { return creationDate; }
    public void setCreationDate(Instant creationDate) { this.creationDate = creationDate; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
