package com.application.animalshelter.controllers;

import com.application.animalshelter.entıty.Animal;
import com.application.animalshelter.service.AnimalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AnimalControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnimalService animalService;


    @Test
    void testAddAnimal() throws Exception {
        Animal animal = new Animal();
        animal.setId(1L);
        animal.setName("Test Animal");

        when(animalService.addAnimal(any(Animal.class))).thenReturn(animal);

        mockMvc.perform(MockMvcRequestBuilders.post("/animal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Animal\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Animal"));
    }

    @Test
    void testDeleteAnimal() throws Exception {
        Long animalId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/animal")
                        .param("id", animalId.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(animalService, times(1)).deleteAnimalById(animalId);
    }

    @Test
    void testGetAnimal() throws Exception {
        Long animalId = 1L;
        Animal animal = new Animal();
        animal.setId(animalId);
        animal.setName("Test Animal");

        when(animalService.findAnimal(animalId)).thenReturn(Optional.of(animal));

        mockMvc.perform(MockMvcRequestBuilders.get("/animal")
                        .param("id", animalId.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Animal"));
    }

    @Test
    void testGetAllAnimals() throws Exception {
        Animal animal1 = new Animal();
        animal1.setId(1L);
        animal1.setName("Animal 1");

        Animal animal2 = new Animal();
        animal2.setId(2L);
        animal2.setName("Animal 2");

        when(animalService.getAllAnimal()).thenReturn(List.of(animal1, animal2));

        mockMvc.perform(MockMvcRequestBuilders.get("/animal/getAll"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Animal 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Animal 2"));
    }
}