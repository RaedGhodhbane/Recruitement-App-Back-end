package com.app.recruitmentapp.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class CandidacyDTO {
    private Long id;
    private Date submissionDate;
    private String status;
    private double score;
    @NotNull(message = "Le candidat est requis")
    private Long candidateId;
    @NotNull(message = "L'offre est requise")
    private Long offerId;

    public CandidacyDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Date getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(Date submissionDate) { this.submissionDate = submissionDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
    public Long getCandidateId() { return candidateId; }
    public void setCandidateId(Long candidateId) { this.candidateId = candidateId; }
    public Long getOfferId() { return offerId; }
    public void setOfferId(Long offerId) { this.offerId = offerId; }
}
