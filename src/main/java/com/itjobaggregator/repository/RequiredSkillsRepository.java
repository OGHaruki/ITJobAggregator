package com.itjobaggregator.repository;

import com.itjobaggregator.model.JobLocation;
import com.itjobaggregator.model.RequiredSkills;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequiredSkillsRepository extends JpaRepository<RequiredSkills, Long> {
    Optional<RequiredSkills> findRequiredSkillsByName(String name);
}
