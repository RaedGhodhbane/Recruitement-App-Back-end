package com.app.recruitmentapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.sql.Date;

public class ExperienceDTO {
    private Long id;
    @NotBlank(message = "Le nom de l'entreprise est requis")
    private String companyName;
    @NotBlank(message = "Le titre du poste est requis")
    private String jobTitle;
    @NotNull(message = "La date de début est requise")
    @PastOrPresent(message = "La date de début doit être dans le passé ou le présent")
    private Date startExpDate;
    private Date endExpDate;
    @NotBlank(message = "La description est requise")
    private String description;
    @NotNull(message = "Le candidat est requis")
    private Long candidateId;

    public ExperienceDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public Date getStartExpDate() { return startExpDate; }
    public void setStartExpDate(Date startExpDate) { this.startExpDate = startExpDate; }
    public Date getEndExpDate() { return endExpDate; }
    public void setEndExpDate(Date endExpDate) { this.endExpDate = endExpDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getCandidateId() { return candidateId; }
    public void setCandidateId(Long candidateId) { this.candidateId = candidateId; }
}
