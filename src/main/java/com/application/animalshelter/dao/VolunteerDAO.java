package com.application.animalshelter.dao;

import com.application.animalshelter.entity.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VolunteerDAO extends JpaRepository<Volunteer,Long> {
    @Query(value = "SELECT * FROM volunteer WHERE is_available = ?1", nativeQuery = true)
    List<Volunteer> findByIsAvailable(Boolean isAvailable);
}
