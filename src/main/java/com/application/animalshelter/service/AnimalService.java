package com.application.animalshelter.service;

import com.application.animalshelter.entıty.Animal;
import java.util.Collection;

public interface AnimalService {
    Animal addAnimal(Animal animal);
    Animal findAnimal(Long id);
    Collection<Animal> getAllAnimal();
    void deleteAnimalById(Long id);
}
