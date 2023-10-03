package com.application.animalshelter.entıty;

import com.application.animalshelter.enums.AnimalType;
import com.application.animalshelter.enums.CityName;
import com.application.animalshelter.enums.CountryName;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.Objects;


@Entity
public class Shelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private CountryName countryName;

    @Enumerated(EnumType.STRING)
    private CityName cityName;

    @Enumerated(EnumType.STRING)
    private AnimalType animalType;

    private String address;
    private String workingHours;
    private String passRules;
    private String shelterRules;

    @JsonManagedReference
    @OneToMany(mappedBy = "shelter")
    private Collection<Animal> animals;

    @JsonManagedReference
    @OneToMany(mappedBy = "shelter")
    private Collection<Volunteer> volunteers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CountryName getCountryName() {
        return countryName;
    }

    public void setCountryName(CountryName countryName) {
        this.countryName = countryName;
    }

    public CityName getCityName() {
        return cityName;
    }

    public void setCityName(CityName cityName) {
        this.cityName = cityName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public String getPassRules() {
        return passRules;
    }

    public void setPassRules(String passRules) {
        this.passRules = passRules;
    }

    public String getShelterRules() {
        return shelterRules;
    }

    public void setShelterRules(String shelterRules) {
        this.shelterRules = shelterRules;
    }

    public AnimalType getAnimalType() {
        return animalType;
    }

    public void setAnimalType(AnimalType animalType) {
        this.animalType = animalType;
    }

    public Collection<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(Collection<Animal> animals) {
        this.animals = animals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shelter shelter = (Shelter) o;
        return Objects.equals(id, shelter.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Shelter{" +
                "id=" + id +
                ", countryName=" + countryName +
                ", cityName=" + cityName +
                ", address='" + address + '\'' +
                ", workingHours='" + workingHours + '\'' +
                ", passRules='" + passRules + '\'' +
                ", shelterRules='" + shelterRules + '\'' +
                ", animalType=" + animalType +
                ", animals=" + animals +
                '}';
    }
}
