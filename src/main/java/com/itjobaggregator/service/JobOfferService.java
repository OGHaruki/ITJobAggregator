package com.itjobaggregator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itjobaggregator.model.JobLocation;
import com.itjobaggregator.model.JobOffer;
import com.itjobaggregator.model.JobSource;
import com.itjobaggregator.repository.JobLocationRepository;
import com.itjobaggregator.repository.JobOfferRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class JobOfferService {

    @Autowired
    private JobOfferRepository jobOfferRepository;

    @Autowired
    private JobLocationRepository jobLocationRepository;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public JobOfferService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    @Transactional
    public void fetchAndSaveJobOffers() throws JsonProcessingException {
        // Fetch job offers from JustJoinIT API
        String justJoinItUrl = "https://api.justjoin.it/v2/user-panel/offers?&page=2&sortBy=published&orderBy=DESC&perPage=100&salaryCurrencies=PLN";
        String justJoinItResponse = fetchJobOffers(justJoinItUrl);

        // Parse and save job offers to database
        if (justJoinItResponse != null) {
            List<JobOffer> jobOfferList = parseJustJoinItResponse(justJoinItResponse);
            jobOfferRepository.saveAll(jobOfferList);
        }
    }

    private String fetchJobOffers(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response;

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            Logger.getLogger(JobOfferService.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return response.body();
    }

    private List<JobOffer> parseJustJoinItResponse(String justJoinItResponse) throws JsonProcessingException {

        JsonNode rootNode = objectMapper.readTree(justJoinItResponse);
        JsonNode jobOffersNode = rootNode.get("data");
        List<JobOffer> jobOfferList = new ArrayList<>();

        for (JsonNode jobNode : jobOffersNode) {
            JobOffer jobOffer = new JobOffer();
            jobOffer.setTitle(jobNode.get("title").asText());
            jobOffer.setCompanyName(jobNode.get("companyName").asText());
            jobOffer.setWorkplaceType(jobNode.get("workplaceType").asText());
            jobOffer.setExperienceLevel(jobNode.get("experienceLevel").asText());
            jobOffer.setRawData(jobNode.toString());
            jobOffer.setSource(JobSource.JustJoinIT);

            List<String> requiredSkills = new ArrayList<>();
            if (jobNode.has("requiredSkills")) {
                for (JsonNode skillNode : jobNode.get("requiredSkills")) {
                    requiredSkills.add(skillNode.asText());
                }
            }
            jobOffer.setRequiredSkills(requiredSkills);

            if(jobNode.has("multilocation")) {
                for (JsonNode locationNode : jobNode.get("multilocation")) {
                    String city = locationNode.get("city").asText();
                    Double latitude = locationNode.has("latitude") ? locationNode.get("latitude").asDouble() : null;
                    Double longitude = locationNode.has("longitude") ? locationNode.get("longitude").asDouble() : null;

                    // Check if location already exists in database
                    Optional<JobLocation> existingLocation = jobLocationRepository.findJobLocationByCity(city);
                    if(existingLocation.isPresent()) {
                        jobOffer.addJobLocation(existingLocation.get());
                    } else {
                        JobLocation jobLocation = new JobLocation();
                        jobLocation.setCity(city);
                        jobLocation.setLatitude(latitude);
                        jobLocation.setLongitude(longitude);
                        jobLocationRepository.save(jobLocation);
                        jobOffer.addJobLocation(jobLocation);
                    }
                }
            }
            jobOfferList.add(jobOffer);
        }
        return jobOfferList;
    }
}
