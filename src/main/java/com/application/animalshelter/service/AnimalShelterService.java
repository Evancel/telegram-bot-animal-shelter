package com.application.animalshelter.service;

import com.application.animalshelter.entity.AnimalShelter;
import com.application.animalshelter.enums.City;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AnimalShelterService {
    AnimalShelter saveAnimalShelter(AnimalShelter animalShelter);
    AnimalShelter updateAddress(Long id, String address);
    void uploadPassRules(Long id, MultipartFile passRules) throws IOException;
    AnimalShelter getAnimalShelter(Long Id);
    List<AnimalShelter> getAnimalShelters();
    List<AnimalShelter> getAnimalSheltersByCity(City city);
    void deleteAnimalShelter(Long Id);

}
