package com.itjobaggregator.controller;

import com.itjobaggregator.model.JobOffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itjobaggregator.repository.JobOfferRepository;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class JobOfferController {

    @Autowired
    private JobOfferRepository jobOfferRepository;

    @RequestMapping("/")
    public String home() {
        return "Hello, World!";
    }

    @GetMapping("/job_offers")
    public ResponseEntity<List<JobOffer>> getAllJobOffers() {
        return ResponseEntity.ok(jobOfferRepository.findAll());
    }
}
