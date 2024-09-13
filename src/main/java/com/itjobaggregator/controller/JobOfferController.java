package com.itjobaggregator.controller;

import com.itjobaggregator.model.JobOffer;
import com.itjobaggregator.service.JobOfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

@CrossOrigin(origins = "http://localhost:8081")
@RequestMapping("/api")
@RestController
@Tag(name = "Job Offer Controller", description = "Endpoints for fetching and retrieving job offers")
public class JobOfferController {

    private final JobOfferService jobOfferService;

    @Autowired
    public JobOfferController(JobOfferService jobOfferService) {
        this.jobOfferService = jobOfferService;
    }
    @GetMapping("/home")
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

    /*@GetMapping("/offers")
    public ResponseEntity<List<JobOffer>> getJobOffers(
            @RequestParam(name= "tech", required = false) List<String> tech,
            @RequestParam(name = "seniority", required = false) List<String> seniority,
            @RequestParam(name = "location", required = false) List<String> location,
            @RequestParam(name = "from", required = false) LocalDate from,
            @RequestParam(name = "to", required = false) LocalDate to
    ) {
        List<JobOffer> jobOffers = jobOfferService.getJobOffers(tech, seniority, location, from, to);

        if(jobOffers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(jobOffers);
    }*/
}
