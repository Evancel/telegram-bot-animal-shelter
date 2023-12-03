package com.application.animalshelter.listener;

import com.application.animalshelter.entity.AnimalShelter;
import com.application.animalshelter.service.AnimalShelterService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * API for volunteers
 */

@RestController
@RequestMapping("/animal-shelter")
public class VolunteerController {
    private final AnimalShelterService animalShelterService;

    public VolunteerController(AnimalShelterService animalShelterService) {
        this.animalShelterService = animalShelterService;
    }

    /**
     * a volunteer can add a new shelter or change an existing shelter
     * @param animalShelter must not be null
     * @return ResponseEntity 200 code
     * @see org.springframework.data.jpa.repository.JpaRepository#save(Object)
     */

    @PostMapping()
    public ResponseEntity<AnimalShelter> addNewShelter(@RequestBody AnimalShelter animalShelter){
        if(animalShelter==null){
            return ResponseEntity.notFound().build();
        }

        AnimalShelter savedAnimalShelter = animalShelterService.saveAnimalShelter(animalShelter);
        return ResponseEntity.ok(savedAnimalShelter);
    }


    /**
     * a volunteer can add an address of the shelter or change an existing address
     * @param id must not be null
     * @return ResponseEntity 200 code
     * @see org.springframework.data.jpa.repository.JpaRepository#save(Object)
     */
    @PostMapping("/{id}/address")
    public ResponseEntity<AnimalShelter> updateAddress(@PathVariable Long id, @RequestBody String address){
        if(id==null || animalShelterService.getAnimalShelter(id)==null){
            return ResponseEntity.notFound().build();
        }
        AnimalShelter savedAnimalShelter = animalShelterService.updateAddress(id, address);

        return ResponseEntity.ok(savedAnimalShelter);
    }

    /**
     * a volunteer can add a pass rules schema of the shelter or change the existing pass rules schema
     * @param id must not be null
     * @return ResponseEntity 200 code
     * @see org.springframework.data.jpa.repository.JpaRepository#save(Object)
     */
    @PostMapping(value = "/{id}/passRules", consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updatePassRules(@PathVariable Long id,
                                                         @RequestBody MultipartFile passRules) throws IOException {

        if (id == null || animalShelterService.getAnimalShelter(id) == null) {
            return ResponseEntity.notFound().build();
        }

        if (passRules.getSize() > 1024 * 3000) {
            return ResponseEntity.badRequest().body("The image is too big!");
        }

        //add a verification about the extension of the file: .jpg, .png
        if (!passRules.getContentType().equals("image/jpeg") && !passRules.getContentType().equals("image/png")) {
            return ResponseEntity.badRequest().body("The wrong type of the file.");
        }

        animalShelterService.uploadPassRules(id, passRules);
        return ResponseEntity.ok().build();
    }

    /**
     * a volunteer can get the existing shelter by its id
     * @param id must not be null
     * @return ResponseEntity 200 code
     * @see org.springframework.data.jpa.repository.JpaRepository#save(Object)
     */
    @GetMapping("/{id}")
    public ResponseEntity<AnimalShelter> getShelter(@PathVariable Long id){
        AnimalShelter animalShelter = animalShelterService.getAnimalShelter(id);
        return ResponseEntity.ok(animalShelter);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AnimalShelter>> getAllShelters(){
        List<AnimalShelter> animalShelters = animalShelterService.getAnimalShelters();
        return ResponseEntity.ok(animalShelters);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnimalShelter(@PathVariable Long id){
        animalShelterService.deleteAnimalShelter(id);
        return ResponseEntity.ok().build();
    }

}
