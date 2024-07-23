package com.itjobaggregator.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String companyName;
    private String workplaceType;
    private String experienceLevel;
    private String rawData;
    // private String technologyStack;  Brakuje go w api justjoinit

    @Enumerated(EnumType.STRING)
    private JobSource source;

    @ElementCollection
    private List<String> requiredSkills;

    @OneToMany(mappedBy = "jobOffer", cascade = CascadeType.ALL)
    private List<JobLocation> jobLocations = new ArrayList<>();

    public void addJobLocation(JobLocation jobLocation) {
        jobLocations.add(jobLocation);
        jobLocation.setJobOffer(this);
    }

    public void removeJobLocation(JobLocation jobLocation) {
        jobLocations.remove(jobLocation);
        jobLocation.setJobOffer(null);
    }
}

