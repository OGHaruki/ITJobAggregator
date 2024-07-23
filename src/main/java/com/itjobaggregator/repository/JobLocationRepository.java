package com.itjobaggregator.repository;

import com.itjobaggregator.model.JobLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobLocationRepository extends JpaRepository<JobLocation, Long> {
    Optional<JobLocation> findJobLocationByCity(String city);
}
