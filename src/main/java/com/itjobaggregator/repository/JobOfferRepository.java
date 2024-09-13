package com.itjobaggregator.repository;

import com.itjobaggregator.model.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {

    Optional<JobOffer> findFirstBySlug(String slug);

    @Query("SELECT DISTINCT j FROM JobOffer j JOIN j.requiredSkills rs JOIN j.jobLocations jl WHERE " +
            "(:tech IS NULL OR rs.name IN :tech) AND " +
            "(:seniority IS NULL OR j.experienceLevel IN :seniority) AND " +
            "(:location IS NULL OR jl.city IN :location) AND " +
            "(:from IS NULL OR j.createdAt >= :from) AND " +
            "(:to IS NULL OR j.createdAt <= :to)")
    List<JobOffer> findJobOffers(@Param("tech") List<String> tech,
                                 @Param("seniority") List<String> seniority,
                                 @Param("location") List<String> location,
                                 @Param("from") LocalDate from,
                                 @Param("to") LocalDate to);
}
