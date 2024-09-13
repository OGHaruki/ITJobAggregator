package com.itjobaggregator.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.itjobaggregator.service.JobOfferService;
import jakarta.persistence.*;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.*;

@Entity
@Data
@Table(name = "job_offer")
public class JobOffer {

    private static final Logger log = LoggerFactory.getLogger(JobOfferService.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String slug;
    private String title;
    private String companyName;
    private String workplaceType;
    private String experienceLevel;
    private LocalDate createdAt;

    @Enumerated(EnumType.STRING)
    private JobSource source;

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    @JoinTable(
            name = "job_offer_required_skills",
            joinColumns = @JoinColumn(name = "job_offer_id"),
            inverseJoinColumns = @JoinColumn(name = "required_skills_id")
    )
    private Set<RequiredSkills> requiredSkills = new HashSet<>();

    @ManyToMany
    @JsonManagedReference
    @JoinTable(
            name = "job_offer_location",
            joinColumns = @JoinColumn(name = "job_offer_id"),
            inverseJoinColumns = @JoinColumn(name = "job_location_id")
    )
    private Set<JobLocation> jobLocations = new HashSet<>();

    public void addRequiredSkill(RequiredSkills skill) {
        this.requiredSkills.add(skill);
        skill.getJobOffers().add(this);
    }

    public void removeRequiredSkill(RequiredSkills skill) {
        this.requiredSkills.remove(skill);
        skill.getJobOffers().remove(this);
    }

    public void addJobLocation(JobLocation jobLocation) {
        List<String> cities = new ArrayList<>();
        this.jobLocations.forEach(location -> cities.add(location.getCity()));
        if (!cities.contains(jobLocation.getCity())) {
            this.jobLocations.add(jobLocation);
            jobLocation.getJobOffers().add(this);
        }
    }

    public void removeJobLocation(JobLocation jobLocation) {
        jobLocations.remove(jobLocation);
        jobLocation.getJobOffers().remove(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, slug, title, companyName, workplaceType, experienceLevel, source, createdAt);
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
                ", createdAt=" + createdAt +
                '}';
    }
}

