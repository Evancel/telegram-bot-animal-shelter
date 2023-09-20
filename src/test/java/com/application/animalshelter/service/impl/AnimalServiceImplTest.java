package com.application.animalshelter.service.impl;

import com.application.animalshelter.dao.AnimalDAO;
import com.application.animalshelter.entıty.Animal;
import com.application.animalshelter.service.AnimalService;
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
public class AnimalServiceImplTest {

    @Mock
    private AnimalDAO animalDAO;

    private AnimalService out;

    @BeforeEach
    void init() {
        out = new AnimalServiceImpl(animalDAO);
    }

    @Test
    void testAddAnimal() {
        Animal animal = new Animal(); // Создаем тестовое животное

        when(animalDAO.save(animal)).thenReturn(animal); // Мокируем метод сохранения

        Animal savedAnimal = out.addAnimal(animal);

        // Проверяем, что метод save был вызван один раз с нашим животным
        verify(animalDAO, times(1)).save(animal);

        // Проверяем, что результат метода совпадает с возвращаемым значением
        assertSame(savedAnimal, animal);
    }

    @Test
    void testFindAnimal() {
        Long animalId = 1L; // Идентификатор тестового животного
        Animal animal = new Animal(); // Создаем тестовое животное

        when(animalDAO.findById(animalId)).thenReturn(Optional.of(animal)); // Мокируем метод поиска по ID

        Optional<Animal> foundAnimal = out.findAnimal(animalId);

        // Проверяем, что метод findById был вызван один раз с нужным ID
        verify(animalDAO, times(1)).findById(animalId);

        // Проверяем, что найденное животное совпадает с возвращаемым значением
        assertTrue(foundAnimal.isPresent());
        assertSame(foundAnimal.get(), animal);
    }

    @Test
    void testGetAllAnimal() {
        // Создаем список тестовых животных
        List<Animal> animals = List.of(new Animal(), new Animal());

        when(animalDAO.findAll()).thenReturn(animals); // Мокируем метод получения всех животных

        Collection<Animal> allAnimals = out.getAllAnimal();

        // Проверяем, что метод findAll был вызван один раз
        verify(animalDAO, times(1)).findAll();

        // Проверяем, что список всех животных совпадает с возвращаемым значением
        assertEquals(allAnimals, animals);
    }

    @Test
    void testDeleteAnimalById() {
        Long animalId = 1L; // Идентификатор тестового животного

        out.deleteAnimalById(animalId);

        // Проверяем, что метод deleteById был вызван один раз с нужным ID
        verify(animalDAO, times(1)).deleteById(animalId);
    }
}