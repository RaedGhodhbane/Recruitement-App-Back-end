package com.app.recruitmentapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SkillDTO {
    private Long id;
    @NotBlank(message = "Le titre est requis")
    private String title;
    @NotBlank(message = "Le pourcentage est requis")
    private String percentage;
    @NotNull(message = "Le candidat est requis")
    private Long candidateId;

    public SkillDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getPercentage() { return percentage; }
    public void setPercentage(String percentage) { this.percentage = percentage; }
    public Long getCandidateId() { return candidateId; }
    public void setCandidateId(Long candidateId) { this.candidateId = candidateId; }
}
