package com.itjobaggregator.controller;

import com.itjobaggregator.model.JobOffer;
import com.itjobaggregator.service.JobOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
public class JobOfferController {

    private final JobOfferService jobOfferService;

    @Autowired
    public JobOfferController(JobOfferService jobOfferService) {
        this.jobOfferService = jobOfferService;
    }
    @RequestMapping("/home")
    public String home() {
        return "Hello, World!";
    }

    @GetMapping("/fetch-job-offers")
    public String fetchJobOffers() {
        try {
            jobOfferService.fetchAndSaveJobOffers();
            return "Job offers fetched and saved successfully!";
        } catch (Exception e) {
            return "An error occurred while fetching job offers: " + e.getMessage();
        }
    }

    @GetMapping("/job-offers")
    public ResponseEntity<List<JobOffer>> getJobOffers() {
        return ResponseEntity.ok(jobOfferService.getJobOffers());
    }

    @GetMapping("/job-offers/java")
    public ResponseEntity<Optional<List<JobOffer>>> getJavaJobOffers() {
        return ResponseEntity.ok(jobOfferService.getJobOffersByTechnology("Java"));
    }
}
