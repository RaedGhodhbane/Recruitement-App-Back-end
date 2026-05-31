package com.app.recruitmentapp.dto;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.util.Date;

public class CandidateDTO extends UserDTO {
    private String cv;
    private String cvPath;
    private String description;
    private String address;
    private String title;
    private String image;
    @Pattern(regexp = "^\\+?[0-9\\s\\-]{8,15}$", message = "Le numéro de téléphone est invalide")
    private String phone;
    @Past(message = "La date de naissance doit être dans le passé")
    private Date dateOfBirth;
    private String gender;
    private boolean active;

    public CandidateDTO() {}

    public String getCv() { return cv; }
    public void setCv(String cv) { this.cv = cv; }
    public String getCvPath() { return cvPath; }
    public void setCvPath(String cvPath) { this.cvPath = cvPath; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
