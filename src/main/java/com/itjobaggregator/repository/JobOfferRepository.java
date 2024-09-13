package com.itjobaggregator.repository;

import com.itjobaggregator.model.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JobOfferRepository extends JpaRepository<JobOffer, Long>, CustomJobOfferRepository {

    Optional<JobOffer> findFirstBySlug(String slug);

    List<JobOffer> findAllBySlugIn(List<String> slugs);

    Optional<JobOffer> findFirstJobOfferBySlug(String slug);

    boolean existsBySlug(String slug);
}
