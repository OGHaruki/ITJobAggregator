package com.itjobaggregator.service;

import com.itjobaggregator.model.JobOffer;
import com.itjobaggregator.repository.JobLocationRepository;
import com.itjobaggregator.repository.JobOfferRepository;
import com.itjobaggregator.repository.RequiredSkillsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JobOfferServiceTest {

    @InjectMocks
    private JobOfferService jobOfferService;

    @Mock
    private JobOfferRepository jobOfferRepository;

    @Mock
    private JobLocationRepository jobLocationRepository;

    @Mock
    private RequiredSkillsRepository requiredSkillsRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void fetchJobOfferIfNotExistThenSaveToDatabase() {
        JobOffer newJobOffer = new JobOffer();
        newJobOffer.setTitle("Java Developer");
        newJobOffer.setCompanyName("Non-existing-location");

        when(jobOfferRepository.findFirstBySlug(newJobOffer.getSlug())).thenReturn(Optional.empty());

        jobOfferService.saveNewJobOffers(Collections.singletonList(newJobOffer));

        verify(jobOfferRepository, times(1)).save(newJobOffer);

        assertEquals("Java Developer", newJobOffer.getTitle());
        assertEquals("Non-existing-location", newJobOffer.getCompanyName());
    }

    @Test
    public void fetchJobOfferIfExistThenNotSaveToDatabase() {
        JobOffer newJobOffer = new JobOffer();
        newJobOffer.setTitle("Java Developer");
        newJobOffer.setCompanyName("Non-existing-location");

        when(jobOfferRepository.findFirstBySlug(newJobOffer.getSlug())).thenReturn(Optional.of(newJobOffer));

        jobOfferService.saveNewJobOffers(Collections.singletonList(newJobOffer));

        verify(jobOfferRepository, times(0)).save(newJobOffer);
    }

    @Test
    public void testFindFirstJobOfferBySlug() {
        String slug = "example-slug";
        JobOffer jobOffer = new JobOffer();
        jobOffer.setSlug(slug);

        when(jobOfferRepository.findFirstBySlug(slug)).thenReturn(Optional.of(jobOffer));

        Optional<JobOffer> result = jobOfferRepository.findFirstBySlug(slug);

        assertTrue(result.isPresent());
        assertEquals(slug, result.get().getSlug());
    }
}
