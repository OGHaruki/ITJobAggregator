package com.itjobaggregator.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@Table(name = "required_skills")
public class RequiredSkills {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "requiredSkills")
    @JsonBackReference
    private Set<JobOffer> jobOffers = new HashSet<>();

    public void addJobOffer(JobOffer jobOffer) {
        this.jobOffers.add(jobOffer);   // Add the job offer to the linked set of job offers
        jobOffer.getRequiredSkills().add(this); // Add the skill to the set of required skills of the job offer
    }

    public void removeJobOffer(JobOffer jobOffer) {
        this.jobOffers.remove(jobOffer);
        jobOffer.getRequiredSkills().remove(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "RequiredSkills{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
