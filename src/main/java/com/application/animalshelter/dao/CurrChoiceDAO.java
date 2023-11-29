package com.application.animalshelter.dao;

import com.application.animalshelter.entıty.AppUser;
import com.application.animalshelter.entıty.CurrChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CurrChoiceDAO extends JpaRepository<CurrChoice,Long> {
    @Query(value = "SELECT MAX(id) FROM curr_choice WHERE app_user_id = ?1", nativeQuery = true)
    Long findLastChoiceByAppUserId(Long appUserId);
    @Query(value = "SELECT animal_shelter_id FROM curr_choice WHERE id = ?1", nativeQuery = true)
    Long findAnimalShelterIdByChoiceId(Long ChoiceId);
}
