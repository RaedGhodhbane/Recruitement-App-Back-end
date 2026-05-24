package com.app.recruitmentapp.dto;

import java.time.Instant;

public class RecruiterDTO extends UserDTO {
    private String address;
    private Instant creationDate;
    private String image;
    private boolean active;
    private String companyName;
    private String phone;
    private String website;
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
