package com.itjobaggregator.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "job_location")
public class JobLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String city;
    private Double latitude;
    private Double longitude;

    @ManyToMany(mappedBy = "jobLocations")
    @JsonBackReference
    private Set<JobOffer> jobOffers = new HashSet<>();

    public void addJobOffer(JobOffer jobOffer) {
        this.jobOffers.add(jobOffer);
        jobOffer.getJobLocations().add(this);
    }

    public void removeJobOffer(JobOffer jobOffer) {
        this.jobOffers.remove(jobOffer);
        jobOffer.getJobLocations().remove(this);
    }
}
