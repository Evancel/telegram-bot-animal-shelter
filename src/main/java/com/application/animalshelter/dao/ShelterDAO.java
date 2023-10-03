package com.application.animalshelter.dao;

import com.application.animalshelter.entıty.Shelter;
import com.application.animalshelter.enums.AnimalType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShelterDAO extends JpaRepository<Shelter, Long> {
    Shelter findByAnimalType(AnimalType animalType);
}
