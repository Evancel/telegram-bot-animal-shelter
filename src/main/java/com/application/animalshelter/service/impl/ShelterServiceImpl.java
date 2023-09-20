package com.application.animalshelter.service.impl;

import com.application.animalshelter.dao.ShelterDAO;
import com.application.animalshelter.entıty.Shelter;
import com.application.animalshelter.service.ShelterService;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Optional;

@Service
public class ShelterServiceImpl implements ShelterService {
    private final ShelterDAO shelterDAO;

    public ShelterServiceImpl(ShelterDAO shelterDAO) {
        this.shelterDAO = shelterDAO;
    }

    @Override
    public Shelter saveShelter(Shelter shelter) {
        return  shelterDAO.save(shelter);
    }

    @Override
    public Optional<Shelter> getShelter(Long Id) {
        return shelterDAO.findById(Id);
    }
    @Override
    public void deleteShelter(Shelter shelter) {
        shelterDAO.delete(shelter);
    }

    @Override
    public Collection<Shelter> getAllShelters() {
        return shelterDAO.findAll();
    }
}
