package com.itjobaggregator.repository;

import com.itjobaggregator.model.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {
    // write my own ideas here later
}
