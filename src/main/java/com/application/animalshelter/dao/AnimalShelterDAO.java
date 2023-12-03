package com.application.animalshelter.dao;

import com.application.animalshelter.entity.AnimalShelter;
import com.application.animalshelter.entity.PassRules;
import com.application.animalshelter.enums.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnimalShelterDAO extends JpaRepository<AnimalShelter, Long> {
    List<AnimalShelter> findByCity(City city);
    @Query(value = "SELECT working_hours FROM animal_shelter WHERE id = ?1", nativeQuery = true)
    String findWorkingHoursById(Long animalShelterId);
}