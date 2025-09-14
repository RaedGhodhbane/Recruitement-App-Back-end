package com.app.recruitmentapp.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin extends User {
    private boolean active;

    private String gender;

    private Date birthdate;

    private String address;

    private String city;

    private String state;

    private String country;

    private String phone;

    @Column(columnDefinition = "LONGTEXT")
    private String image;


    public Admin(Long id, String name, String firstName, String email, String password, Role role, List<Message> sendMessageList, List<Message> receiveMessageList, List<Favourite> favouriteList, boolean active, String gender, Date birthdate, String address, String city, String state, String country, String phone, String image) {
        super(id, name, firstName, email, password, role, sendMessageList, receiveMessageList, favouriteList);
        this.active = active;
        this.gender = gender;
        this.birthdate = birthdate;
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.phone = phone;
        this.image = image;
    }


    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
