package com.itjobaggregator.repository;

import com.itjobaggregator.model.JobOffer;

import java.util.Optional;

public interface CustomJobOfferRepository {
    Optional<JobOffer> findFirstJobOfferBySlug(String slug);
}
