package com.application.animalshelter.entıty;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private Long telegramUserId;

    /*добавит текущую дату на момент сохранения данных в БД*/
    @CreationTimestamp
    private LocalDateTime firstLoginDate;

    private String phoneNumber;
    private String shelterType;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private Boolean isActive;

    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private Collection<Animal> animals;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getTelegramUserId() {
        return telegramUserId;
    }

    public void setTelegramUserId(Long telegramUserId) {
        this.telegramUserId = telegramUserId;
    }

    public LocalDateTime getFirstLoginDate() {
        return firstLoginDate;
    }

    public void setFirstLoginDate(LocalDateTime firstLoginDate) {
        this.firstLoginDate = firstLoginDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getShelterType() {
        return shelterType;
    }

    public void setShelterType(String animalType) {
        this.shelterType = animalType;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUser user = (AppUser) o;
        return Objects.equals(telegramUserId, user.telegramUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(telegramUserId);
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "Id=" + Id +
                ", telegramUserId=" + telegramUserId +
                ", firstLoginDate=" + firstLoginDate +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
