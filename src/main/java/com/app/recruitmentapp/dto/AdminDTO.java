package com.app.recruitmentapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.sql.Date;

public class AdminDTO extends UserDTO {
    private boolean active;
    private String gender;
    @Past(message = "La date de naissance doit être dans le passé")
    private Date birthdate;
    @NotBlank(message = "L'adresse est requise")
    private String address;
    @NotBlank(message = "La ville est requise")
    private String city;
    private String state;
    @NotBlank(message = "Le pays est requis")
    private String country;
    @Pattern(regexp = "^\\+?[0-9\\s\\-]{8,15}$", message = "Le numéro de téléphone est invalide")
    private String phone;
    private String image;

    public AdminDTO() {}

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public Date getBirthdate() { return birthdate; }
    public void setBirthdate(Date birthdate) { this.birthdate = birthdate; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}
