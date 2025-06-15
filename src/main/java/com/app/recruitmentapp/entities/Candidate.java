package com.app.recruitmentapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)

public class Candidate extends User{
    private String cv;
    private String description;
    private String address;
    private String title;
    private String image;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "candidate")
    private List<Candidacy> candidacyList;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "candidate")
    private List<Experience> experienceList;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "candidate")
    private List<Skill> skillList;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "candidate")
    private List<Education> educationList;
    private boolean active;

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    private String cvPath;

    public String getCvPath() {
        return cvPath;
    }

    public void setCvPath(String cvPath) {
        this.cvPath = cvPath;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Candidacy> getCandidacyList() {
        return candidacyList;
    }

    public void setCandidacyList(List<Candidacy> candidacyList) {
        this.candidacyList = candidacyList;
    }

    public List<Experience> getExperienceList() {
        return experienceList;
    }

    public void setExperienceList(List<Experience> experienceList) {
        this.experienceList = experienceList;
    }

    public List<Skill> getSkillList() {
        return skillList;
    }

    public void setSkillList(List<Skill> skillList) {
        this.skillList = skillList;
    }

    public List<Education> getEducationList() {
        return educationList;
    }

    public void setEducationList(List<Education> educationList) {
        this.educationList = educationList;
    }
}
