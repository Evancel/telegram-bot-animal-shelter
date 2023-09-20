package com.application.animalshelter.service;

import com.application.animalshelter.entıty.Animal;
import java.util.Collection;
import java.util.Optional;

public interface AnimalService {
    Animal addAnimal(Animal animal);
    Optional<Animal> findAnimal(Long id);
    Collection<Animal> getAllAnimal();
    void deleteAnimalById(Long id);
}
