package com.itjobaggregator.service;


import com.itjobaggregator.model.JobLocation;
import com.itjobaggregator.model.JobOffer;
import com.itjobaggregator.repository.JobLocationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobLocationService {

    @Autowired
    private JobLocationRepository jobLocationRepository;

    @Transactional
    public void saveCityToDatabase(JobOffer jobOffer, String city, Double latitude, Double longitude) {
        Optional<JobLocation> existingLocation = jobLocationRepository.findJobLocationByCity(city);
        JobLocation jobLocation;

        if (existingLocation.isPresent()) {
            jobLocation = existingLocation.get();
        } else {
            jobLocation = new JobLocation();
            jobLocation.setCity(city);
            jobLocation.setLatitude(latitude);
            jobLocation.setLongitude(longitude);
            jobLocationRepository.save(jobLocation);
        }

        jobOffer.addJobLocation(jobLocation);
    }
}
