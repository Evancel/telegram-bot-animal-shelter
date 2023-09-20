package com.application.animalshelter.controllers;


import com.application.animalshelter.entıty.Shelter;
import com.application.animalshelter.service.ShelterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ShelterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // ObjectMapper для сериализации/десериализации JSON

    @MockBean
    private ShelterService shelterService;

    @Test
    public void testAddNewShelter() throws Exception {
        Shelter shelter = new Shelter();
        shelter.setId(1L);
        shelter.setAddress("address");

        Mockito.when(shelterService.saveShelter(Mockito.any(Shelter.class))).thenReturn(shelter);

        mockMvc.perform(MockMvcRequestBuilders.post("/shelter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shelter)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("address"));
    }

    @Test
    public void testGetShelter() throws Exception {
        Long shelterId = 1L;
        Shelter shelter = new Shelter();
        shelter.setId(shelterId);
        shelter.setAddress("address");

        Mockito.when(shelterService.getShelter(shelterId)).thenReturn(Optional.of(shelter));

        mockMvc.perform(MockMvcRequestBuilders.get("/shelter")
                        .param("id", shelterId.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("address"));
    }

    @Test
    public void testDeleteShelter() throws Exception {
        Shelter shelter = new Shelter();
        shelter.setId(1L);
        shelter.setAddress("address");

        mockMvc.perform(MockMvcRequestBuilders.delete("/shelter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shelter)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetAllShelters() throws Exception {
        Shelter shelter1 = new Shelter();
        shelter1.setId(1L);
        shelter1.setAddress("address1");

        Shelter shelter2 = new Shelter();
        shelter2.setId(2L);
        shelter2.setAddress("address2");

        Mockito.when(shelterService.getAllShelters()).thenReturn(List.of(shelter1, shelter2));

        mockMvc.perform(MockMvcRequestBuilders.get("/shelter/getAll"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].address").value("address1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].address").value("address2"));
    }
}