package com.itjobaggregator.service;


import com.itjobaggregator.model.JobLocation;
import com.itjobaggregator.model.JobOffer;
import com.itjobaggregator.repository.JobLocationRepository;
import com.itjobaggregator.repository.JobOfferRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobLocationService {

    @Autowired
    private JobLocationRepository jobLocationRepository;

    private static final Logger log = LoggerFactory.getLogger(JobOfferService.class);

    /*public void saveCityToDatabase(JobOffer jobOffer, String city, Double latitude, Double longitude) {
        Optional<JobLocation> existingLocation = Optional.of(jobLocationRepository.findJobLocationByCity(city)
                .orElseGet(() -> {
                    JobLocation newLocation = new JobLocation();
                    newLocation.setCity(city);
                    newLocation.setLatitude(latitude);
                    newLocation.setLongitude(longitude);
                    return jobLocationRepository.save(newLocation);
                }));

        existingLocation.ifPresent(jobOffer::addJobLocation);
    }*/

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
