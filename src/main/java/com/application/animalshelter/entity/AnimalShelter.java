package com.application.animalshelter.entity;

import com.application.animalshelter.enums.AnimalType;
import com.application.animalshelter.enums.City;
import com.application.animalshelter.enums.Country;
import jakarta.persistence.*;

import java.util.Objects;


@Entity
public class AnimalShelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String name;

    @Enumerated(EnumType.STRING)
    private Country country;

    @Enumerated(EnumType.STRING)
    private City city;

    //TODO переделать в карту
    private String address;
    //TODO добавить схему проезда - картинку

    //TODO переделать в календарь
    private String workingHours;
    @OneToOne
    @JoinColumn(name="pass_rules_id", referencedColumnName = "id")
    private PassRules passRules;
    private String rules;

    @Enumerated(EnumType.STRING)
    private AnimalType animalType;

    public AnimalShelter(){}

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
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

    public PassRules getPassRules() {
        return passRules;
    }

    public void setPassRules(PassRules passRules) {
        this.passRules = passRules;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public AnimalType getAnimalType() {
        return animalType;
    }

    public void setAnimalType(AnimalType animalType) {
        this.animalType = animalType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimalShelter that = (AnimalShelter) o;
        return Objects.equals(Id, that.Id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id);
    }

    @Override
    public String toString() {
        return "AnimalShelter{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                ", country=" + country +
                ", city=" + city +
                ", address='" + address + '\'' +
                ", workingHours='" + workingHours + '\'' +
                ", passRules='" + passRules + '\'' +
                ", rules='" + rules + '\'' +
                ", animalType=" + animalType +
                '}';
    }
}
