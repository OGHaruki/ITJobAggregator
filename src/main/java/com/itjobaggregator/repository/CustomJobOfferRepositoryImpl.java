package com.itjobaggregator.repository;

import com.itjobaggregator.model.JobOffer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class CustomJobOfferRepositoryImpl implements CustomJobOfferRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<JobOffer> findFirstJobOfferBySlug(String slug) {
        String jpql = "SELECT job FROM JobOffer job WHERE job.slug = :slug";

        TypedQuery<JobOffer> query = entityManager.createQuery(jpql, JobOffer.class);
        query.setParameter("slug", slug);
        query.setMaxResults(1);

        System.out.println("CustomJobOfferRepositoryImpl.findFirstJobOfferBySlug: " +
                query.getResultList().stream().findFirst());

        return query.getResultList().stream().findFirst();
    }

    /*@Override
    public Optional<List<JobOffer>> findJobOfferByTechStack(String stack) {
        String jpql = "SELECT job FROM JobOffer job JOIN job.requiredSkills skills WHERE skills.skill = :stack";

        TypedQuery<JobOffer> query = entityManager.createQuery(jpql, JobOffer.class);
        query.setParameter("stack", stack);

        return Optional.of(query.getResultList());
    }*/
}
