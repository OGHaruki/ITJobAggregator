package com.itjobaggregator.model;

import jakarta.persistence.*;
import lombok.*;

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
    private String publishedAt;
    // private String technologyStack;  Brakuje go w api justjoinit
    // private String location;         Dodać wiele ofert, każda z różną lokalizacją lub dodawać według lokalizacji
    private String rawData;

    @Enumerated(EnumType.STRING)
    private JobSource source;

    @ElementCollection
    private List<String> requiredSkills;
}
