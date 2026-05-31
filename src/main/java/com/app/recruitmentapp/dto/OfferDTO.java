package com.app.recruitmentapp.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

public class OfferDTO {
    private Long id;
    @NotBlank(message = "Le titre est requis")
    private String title;
    @NotBlank(message = "La description est requise")
    private String description;
    @NotBlank(message = "Le type est requis")
    private String type;
    @NotBlank(message = "L'adresse est requise")
    private String address;
    @PositiveOrZero(message = "Le salaire doit être positif ou nul")
    private double salary;
    @NotBlank(message = "L'expérience est requise")
    private String experience;
    private LocalDate publicationDate;
    @Future(message = "La date d'expiration doit être dans le futur")
    private LocalDate expirationDate;
    @NotNull(message = "Le recruteur est requis")
    private Long recruiterId;

    public OfferDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }
    public LocalDate getPublicationDate() { return publicationDate; }
    public void setPublicationDate(LocalDate publicationDate) { this.publicationDate = publicationDate; }
    public LocalDate getExpirationDate() { return expirationDate; }
    public void setExpirationDate(LocalDate expirationDate) { this.expirationDate = expirationDate; }
    public Long getRecruiterId() { return recruiterId; }
    public void setRecruiterId(Long recruiterId) { this.recruiterId = recruiterId; }
}
