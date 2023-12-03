package com.application.animalshelter.dao;

import com.application.animalshelter.entity.PassRules;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassRulesDao extends JpaRepository<PassRules,Long> {
}
