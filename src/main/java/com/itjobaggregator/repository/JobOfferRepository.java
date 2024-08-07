package com.itjobaggregator.repository;

import com.itjobaggregator.model.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobOfferRepository extends JpaRepository<JobOffer, Long>, CustomJobOfferRepository {
    Optional<JobOffer> findFirstJobOfferBySlug(String slug);

    Optional<JobOffer> findJobOfferBySlug(String slug);
}
