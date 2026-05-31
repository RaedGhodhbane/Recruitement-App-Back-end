package com.app.recruitmentapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EducationDTO {
    private Long id;
    @NotBlank(message = "Le diplôme est requis")
    private String diploma;
    @NotBlank(message = "L'université est requise")
    private String university;
    @NotBlank(message = "La date de fin est requise")
    private String endDate;
    private String description;
    @NotNull(message = "Le candidat est requis")
    private Long candidateId;

    public EducationDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDiploma() { return diploma; }
    public void setDiploma(String diploma) { this.diploma = diploma; }
    public String getUniversity() { return university; }
    public void setUniversity(String university) { this.university = university; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getCandidateId() { return candidateId; }
    public void setCandidateId(Long candidateId) { this.candidateId = candidateId; }
}
