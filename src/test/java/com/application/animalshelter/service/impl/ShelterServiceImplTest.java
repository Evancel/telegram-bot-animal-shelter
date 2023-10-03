package com.application.animalshelter.service.impl;

import com.application.animalshelter.dao.ShelterDAO;
import com.application.animalshelter.entıty.Shelter;
import com.application.animalshelter.service.ShelterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ShelterServiceImplTest {
    @Mock
    private ShelterDAO shelterDAO;

    private ShelterService out;

    @BeforeEach
    void init(){
        out = new ShelterServiceImpl(shelterDAO);
    }

    @Test
    void saveShelter() {
        Shelter shelterToSave = new Shelter(); // Создаем тестовый приют
        when(shelterDAO.save(shelterToSave)).thenReturn(shelterToSave); // Мокируем метод сохранения

        Shelter savedShelter = out.addShelter(shelterToSave);

        // Проверяем, что метод save был вызван один раз с нашим приютом
        verify(shelterDAO, times(1)).save(shelterToSave);

        // Проверяем, что результат метода совпадает с возвращаемым значением
        assertEquals(savedShelter, shelterToSave);
    }

    @Test
    void getShelter() {
        Long shelterId = 1L;
        Shelter shelter = new Shelter();
        when(shelterDAO.findById(shelterId)).thenReturn(Optional.of(shelter)); // Мокируем метод поиска по ID

        Shelter retrievedShelter = out.getShelter(shelterId);

        // Проверяем, что метод findById был вызван один раз с указанным ID
        verify(shelterDAO, times(1)).findById(shelterId);

        // Проверяем, что результат метода совпадает с возвращаемым значением
        assertNotNull(retrievedShelter);
        assertEquals(retrievedShelter, shelter);
    }

    @Test
    void deleteShelter() {
        Shelter shelterToDelete = new Shelter(); // Создаем тестовый приют

        out.deleteShelter(shelterToDelete);

        // Проверяем, что метод delete был вызван один раз с указанным приютом
        verify(shelterDAO, times(1)).delete(shelterToDelete);
    }

    @Test
    void getAllShelters() {
        List<Shelter> shelters = List.of(new Shelter(), new Shelter(), new Shelter()); // Создаем тестовый список приютов
        when(shelterDAO.findAll()).thenReturn(shelters); // Мокируем метод поиска всех приютов

        Collection<Shelter> retrievedShelters = out.getAllShelters();

        // Проверяем, что метод findAll был вызван один раз
        verify(shelterDAO, times(1)).findAll();

        // Проверяем, что результат метода совпадает с возвращаемым значением
        assertEquals(retrievedShelters, shelters);
    }
}