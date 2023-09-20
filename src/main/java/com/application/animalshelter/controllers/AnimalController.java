package com.application.animalshelter.controllers;

import com.application.animalshelter.entıty.Animal;
import com.application.animalshelter.service.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/animal")
public class AnimalController {
    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @Operation(summary = "ДОБАВИТЬ ЖИВОТНОЕ",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Животное добавлено",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Animal.class)
                    )
            )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Введите объект в формате JSON"
            ),
            tags = "Животные"
    )
    @PostMapping()
    public ResponseEntity<Animal> addAnimal(@RequestBody Animal animal) {
        return ResponseEntity.ok(animalService.addAnimal(animal));
    }

    @Operation(summary = "УДАЛИТЬ ПО ID",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Животное удалено",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Animal.class)
                    )
            )
            },
            tags = "Животные"
    )
    @DeleteMapping()
    public ResponseEntity<Animal> deleteAnimal(@Parameter(description = "ID животного", example = "1") @RequestParam Long id) {
        animalService.deleteAnimalById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "ПОЛУЧИТЬ ПО ID",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Получено животное",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Animal.class)
                    )
            )
            },
            tags = "Животные"
    )
    @GetMapping()
    public ResponseEntity<Optional<Animal>> getAnimal(@Parameter(description = "ID животного", example = "1") @RequestParam Long id) {
        return ResponseEntity.ok(animalService.findAnimal(id));
    }

    @Operation(summary = "ПОЛУЧИТЬ ВСЕХ ЖИВОТНЫХ",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Полученные животные",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Animal.class))
                    )
            )
            },
            tags = "Животные"
    )
    @GetMapping("/getAll")
    public ResponseEntity<Collection<Animal>> getAllAnimals() {
        return ResponseEntity.ok(animalService.getAllAnimal());
    }
}
