package com.application.animalshelter.service;

import com.application.animalshelter.entıty.Shelter;
import java.util.Collection;
import java.util.Optional;

public interface ShelterService {
    Shelter saveShelter(Shelter shelter);
    Optional<Shelter> getShelter(Long Id);
    void deleteShelter(Shelter shelter);
    Collection<Shelter> getAllShelters();

}
