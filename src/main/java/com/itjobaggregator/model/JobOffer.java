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
    private String slug;
    private String title;
    private String companyName;
    private String workplaceType;
    private String experienceLevel;
    //private String rawData;
    // private String technologyStack;  Brakuje go w api justjoinit

    @Enumerated(EnumType.STRING)
    private JobSource source;

    @OneToMany(mappedBy = "jobOffer", cascade = CascadeType.ALL)
    private List<RequiredSkills> requiredSkills = new ArrayList<>();

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

    public void addRequiredSkill(RequiredSkills requiredSkill) {
        requiredSkills.add(requiredSkill);
        requiredSkill.setJobOffer(this);
    }

    public void removeRequiredSkill(RequiredSkills requiredSkill) {
        requiredSkills.remove(requiredSkill);
        requiredSkill.setJobOffer(null);
    }

    @Override
    public String toString() {
        return "JobOffer{" +
                "id=" + id +
                ", slug='" + slug + '\'' +
                ", title='" + title + '\'' +
                ", companyName='" + companyName + '\'' +
                ", workplaceType='" + workplaceType + '\'' +
                ", experienceLevel='" + experienceLevel + '\'' +
                ", source=" + source +
                '}';
    }
}

