package com.application.animalshelter.service.impl;

import com.application.animalshelter.dao.AnimalDAO;
import com.application.animalshelter.entıty.Animal;
import com.application.animalshelter.exception.AnimalNotFoundException;
import com.application.animalshelter.service.AnimalService;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Optional;

@Service
public class AnimalServiceImpl implements AnimalService {
    private final AnimalDAO animalDAO;

    public AnimalServiceImpl(AnimalDAO animalDAO) {
        this.animalDAO = animalDAO;
    }

    @Override
    public Animal addAnimal(Animal animal) {
        return animalDAO.save(animal);
    }

    @Override
    public Animal findAnimal(Long id) {
        return animalDAO.findById(id).orElseThrow(()-> new AnimalNotFoundException());
    }

    @Override
    public Collection<Animal> getAllAnimal() {
        return animalDAO.findAll();
    }

    @Override
    public void deleteAnimalById(Long id) {
        animalDAO.deleteById(id);
    }

}
