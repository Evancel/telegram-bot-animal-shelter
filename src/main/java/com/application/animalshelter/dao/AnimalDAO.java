package com.application.animalshelter.dao;

import com.application.animalshelter.entıty.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalDAO extends JpaRepository<Animal, Long> {
}
