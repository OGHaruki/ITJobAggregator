package com.itjobaggregator.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Data
@Table(name = "job_offer")
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

    @Enumerated(EnumType.STRING)
    private JobSource source;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "job_offer_required_skills",
            joinColumns = @JoinColumn(name = "job_offer_id"),
            inverseJoinColumns = @JoinColumn(name = "required_skills_id")
    )
    private Set<RequiredSkills> requiredSkills = new HashSet<>();

    /*@OneToMany(mappedBy = "jobOffer", cascade = CascadeType.ALL)
    private List<JobLocation> jobLocations = new ArrayList<>();*/

    @ManyToMany
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
        this.jobLocations.add(jobLocation);
        jobLocation.getJobOffers().add(this);
    }

    public void removeJobLocation(JobLocation jobLocation) {
        jobLocations.remove(jobLocation);
        jobLocation.getJobOffers().remove(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, slug, title, companyName, workplaceType, experienceLevel, source);
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

