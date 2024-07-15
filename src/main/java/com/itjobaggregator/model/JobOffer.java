package com.itjobaggregator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "job_offers")
public class JobOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @ElementCollection
    private List<String> requiredSkills;

    @ElementCollection
    private List<String> nieToHaveSkills;

    private String workplaceType;

    private String workingTime;

    private String experienceLevel;

    private String companyName;

    @Override
    public String toString() {
        return "JobOffer{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", requiredSkills=" + requiredSkills +
                ", nieToHaveSkills=" + nieToHaveSkills +
                ", workplaceType='" + workplaceType + '\'' +
                ", workingTime='" + workingTime + '\'' +
                ", experienceLevel='" + experienceLevel + '\'' +
                ", companyName='" + companyName + '\'' +
                '}';
    }

}
