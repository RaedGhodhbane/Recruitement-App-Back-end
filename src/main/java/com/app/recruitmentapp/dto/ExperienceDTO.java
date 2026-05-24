package com.app.recruitmentapp.dto;

import java.sql.Date;

public class ExperienceDTO {
    private Long id;
    private String companyName;
    private String jobTitle;
    private Date startExpDate;
    private Date endExpDate;
    private String description;
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
