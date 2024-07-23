package com.itjobaggregator.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String city;
    private Double latitude;
    private Double longitude;

    @ManyToOne
    @JoinColumn(name = "job_offer_id")
    private JobOffer jobOffer;
}
