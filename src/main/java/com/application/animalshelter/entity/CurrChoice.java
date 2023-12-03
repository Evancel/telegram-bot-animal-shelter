package com.application.animalshelter.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class CurrChoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private LocalDateTime currentDateTime;

    @OneToOne
    @JoinColumn(name = "app_user_id", referencedColumnName = "id")
    private AppUser appUser;

    @OneToOne
    @JoinColumn(name = "animal_shelter_id", referencedColumnName = "id")
    private AnimalShelter animalShelter;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public LocalDateTime getCurrentDateTime() {
        return currentDateTime;
    }

    public void setCurrentDateTime(LocalDateTime currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public AnimalShelter getAnimalShelter() {
        return animalShelter;
    }

    public void setAnimalShelter(AnimalShelter animalShelter) {
        this.animalShelter = animalShelter;
    }

    public CurrChoice() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrChoice that = (CurrChoice) o;
        return Objects.equals(Id, that.Id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id);
    }

    @Override
    public String toString() {
        return "CurrChoice{" +
                "Id=" + Id +
                ", currentDateTime=" + currentDateTime +
                ", appUser=" + appUser +
                ", animalShelter=" + animalShelter +
                '}';
    }
}
