package com.itjobaggregator.repository;

import com.itjobaggregator.model.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JobOfferRepository extends JpaRepository<JobOffer, Long>, CustomJobOfferRepository {
    Optional<JobOffer> findFirstJobOfferBySlug(String slug);

    List<JobOffer> findJobOffersByParameters(List<String> tech, List<String> seniority, List<String> location, LocalDate from, LocalDate to);
}
