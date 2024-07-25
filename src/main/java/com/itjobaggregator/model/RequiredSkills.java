package com.itjobaggregator.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequiredSkills {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job_offer_id")
    private JobOffer jobOffer;

    @Override
    public String toString() {
        return "RequiredSkills{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
