package com.itjobaggregator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itjobaggregator.model.JobLocation;
import com.itjobaggregator.model.JobOffer;
import com.itjobaggregator.model.JobSource;
import com.itjobaggregator.model.RequiredSkills;
import com.itjobaggregator.repository.JobLocationRepository;
import com.itjobaggregator.repository.JobOfferRepository;
import com.itjobaggregator.repository.RequiredSkillsRepository;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;

@Service
public class JobOfferService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Autowired
    private JobOfferRepository jobOfferRepository;

    @Autowired
    private JobLocationService jobLocationService;

    @Autowired
    private RequiredSkillService requiredSkillService;

    private static final Logger log = LoggerFactory.getLogger(JobOfferService.class);

    public JobOfferService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    @Transactional
    public void fetchAndSaveJobOffers() {
        log.info("Fetching job offers from JustJoinIt...");
        String justJoinItURL = "https://api.justjoin.it/v2/user-panel/offers?&page=2&sortBy=published&orderBy=DESC&perPage=100&salaryCurrencies=PLN";
        String noFluffJobsURL = "https://nofluffjobs.com/api/joboffers/main?pageTo=2&pageSize=20&withSalaryMatch=true&salaryCurrency=PLN&salaryPeriod=month&region=pl&language=pl-PL";

        try {
            String justJoinItResponse = fetchJobOffersFromJustJoinIt(justJoinItURL);
            String noFluffJobsResponse = fetchJobOffersFromNoFluffJobs(noFluffJobsURL);

            if(justJoinItResponse != null) {
                List<JobOffer> jobOffers = parseJobOffersFromJustJoinIt(justJoinItResponse);
                saveNewJobOffers(jobOffers);
            }
            if(noFluffJobsResponse != null) {
                List<JobOffer> jobOffers = parseJobOffersFromNoFluffJobs(noFluffJobsResponse);
                saveNewJobOffers(jobOffers);
            }
        } catch (Exception e) {
            log.error("An error occurred while fetching job offers from JustJoinIt: " + e.getMessage());
        }
    }

    private String fetchJobOffersFromJustJoinIt(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:128.0) Gecko/20100101 Firefox/128.0")
                .header("Accept", "application/json, text/plain, */*")
                .header("Accept-Language", "pl,en-US;q=0.7,en;q=0.3")
                .header("Accept-Encoding", "identity")
                .header("x-snowplow", "eyJ1c2VySWQiOiJkMDI4NTVmOC0yNDk2LTQ2MDEtOWE0Zi0zMTMzNGZhMTg4YWYiLCJzZXNzaW9uSWQiOiIxY2M1NGQyMi1lNDYyLTQ0ODItOTBkZC01MTA4NWFjNjc0MzgifQ==")
                .header("Version", "2")
                .header("Origin", "https://justjoin.it")
                .header("Referer", "https://justjoin.it/")
                .header("Sec-Fetch-Dest", "empty")
                .header("Sec-Fetch-Mode", "cors")
                .header("Sec-Fetch-Site", "same-site")
                .header("If-None-Match", "W/\"2a035-Ula23U4wUzwWifamzniv23zxa+s\"")
                .header("TE", "trailers")
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                log.warn("Failed to fetch job offers. HTTP Status: {}", response.statusCode());
                return null;
            }
            log.info("Successfully fetched job offers from JustJoinIt.");
            return response.body();
        } catch (IOException | InterruptedException e) {
            log.error("Error while fetching job offers from JustJoinIt", e);
            return null;
        }
    }

    private String fetchJobOffersFromNoFluffJobs(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:128.0) Gecko/20100101 Firefox/128.0")
                .header("Accept", "application/json, text/plain, */*")
                .header("Accept-Language", "pl,en-US;q=0.7,en;q=0.3")
                .header("Accept-Encoding", "identity")
                .header("Referer", "https://nofluffjobs.com/pl/?gad_source=1&gclid=CjwKCAjwko21BhAPEiwAwfaQCKvh7XrN5aXXuM6UPUwfmTHBu8LFrUphQee2soVIUjKrxv1Koun7whoCXaQQAvD_BwE")
                .header("Cache-Control", "no-cache")
                .header("Pragma", "no-cache")
                .header("Sec-Fetch-Dest", "empty")
                .header("Sec-Fetch-Mode", "cors")
                .header("Sec-Fetch-Site", "same-origin")
                .header("Priority", "u=4")
                .header("TE", "trailers")
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                log.warn("Failed to fetch job offers. HTTP Status: {}", response.statusCode());
                return null;
            }
            log.info("Successfully fetched job offers from NoFluffJobs.");
            return response.body();
        } catch (IOException | InterruptedException e) {
            log.error("Error while fetching job offers from NoFluffJobs", e);
            return null;
        }
    }

    private List<JobOffer> parseJobOffersFromJustJoinIt(String response) throws JsonProcessingException {
        JsonNode rootNode = objectMapper.readTree(response);
        JsonNode jobOffersNode = rootNode.get("data");
        List<JobOffer> jobOfferList = new ArrayList<>();

        for (JsonNode jobNode : jobOffersNode) {
            JobOffer jobOffer = new JobOffer();
            jobOffer.setSlug(jobNode.get("slug").asText());
            jobOffer.setTitle(jobNode.get("title").asText());
            jobOffer.setCompanyName(jobNode.get("companyName").asText());
            jobOffer.setWorkplaceType(jobNode.get("workplaceType").asText());
            jobOffer.setExperienceLevel(jobNode.get("experienceLevel").asText());
            jobOffer.setCreatedAt(LocalDate.parse(parseDate(jobNode.get("publishedAt").asText())));
            jobOffer.setSource(JobSource.JustJoinIT);

            List<String> requiredSkills = new ArrayList<>();
            if (jobNode.has("requiredSkills")) {
                for (JsonNode skillNode : jobNode.get("requiredSkills")) {
                    requiredSkills.add(skillNode.asText());
                }
            }

            requiredSkillService.saveSkillsToDatabase(jobOffer, requiredSkills);

            if(jobNode.has("multilocation")) {
                for (JsonNode locationNode : jobNode.get("multilocation")) {
                    String city = locationNode.get("city").asText();
                    Double latitude = locationNode.has("latitude") ? locationNode.get("latitude").asDouble() : null;
                    Double longitude = locationNode.has("longitude") ? locationNode.get("longitude").asDouble() : null;
                    jobLocationService.saveCityToDatabase(jobOffer, city, latitude, longitude);
                }
            }
            jobOfferList.add(jobOffer);
        }
        return jobOfferList;
    }

    private List<JobOffer> parseJobOffersFromNoFluffJobs(String noFluffJobsResponse) throws JsonProcessingException {

        JsonNode rootNode = objectMapper.readTree(noFluffJobsResponse);
        JsonNode jobOffersNode = rootNode.get("postings");
        List<JobOffer> jobOfferList = new ArrayList<>();

        for (JsonNode jobNode : jobOffersNode) {
            JobOffer jobOffer = new JobOffer();
            jobOffer.setSlug(jobNode.get("id").asText());
            jobOffer.setTitle(jobNode.get("title").asText());
            jobOffer.setCompanyName(jobNode.get("name").asText());
            String workplaceType = jobNode.get("fullyRemote").asText();
            if (workplaceType.equals("true")) {
                jobOffer.setWorkplaceType("remote");
            } else {
                jobOffer.setWorkplaceType("hybrid");
            }
            JsonNode seniorityNode = jobNode.get("seniority");
            if (seniorityNode.isArray() && !seniorityNode.isEmpty()) {
                String seniority = seniorityNode.get(0).asText();
                jobOffer.setExperienceLevel(seniority);
            }
            jobOffer.setSource(JobSource.NoFluffJobs);

            List<String> requiredSkills = new ArrayList<>();
            JsonNode skillsNode = jobNode.get("tiles");
            if (skillsNode != null) {
                JsonNode valuesNode = skillsNode.get("values");
                if (valuesNode.isArray() && !valuesNode.isEmpty()) {
                    for (JsonNode valueNode : valuesNode) {
                        if ("requirement".equals(valueNode.get("type").asText())) {
                            requiredSkills.add(valueNode.get("value").asText());
                        }
                    }
                }
            }
            requiredSkillService.saveSkillsToDatabase(jobOffer, requiredSkills);

            JsonNode locationNode = jobNode.get("location");
            if (locationNode != null) {
                JsonNode placesNode = locationNode.get("places");
                if (placesNode.isArray() && !placesNode.isEmpty()) {
                    for (JsonNode placeNode : placesNode) {
                        String city = "";
                        if (placeNode.has("city")) {
                            city = placeNode.get("city").asText();
                        } else if (placeNode.has("province")) {
                            city = placeNode.get("province").asText();
                        }
                        Double latitude = null, longitude = null;
                        if (placeNode.has("geoLocation")) {
                            JsonNode geoLocationNode = placeNode.get("geoLocation");
                            latitude = geoLocationNode.has("latitude") ? geoLocationNode.get("latitude").asDouble() : null;
                            longitude = geoLocationNode.has("longitude") ? geoLocationNode.get("longitude").asDouble() : null;
                        }
                        jobLocationService.saveCityToDatabase(jobOffer, city, latitude, longitude);
                    }
                }
            }
            jobOfferList.add(jobOffer);
        }
        return jobOfferList;
    }

    @Transactional
    void saveNewJobOffers(List<JobOffer> jobOfferList) {
        for (JobOffer jobOffer : jobOfferList) {

            Optional<JobOffer> existingJobOffer = jobOfferRepository.findFirstBySlug(jobOffer.getSlug());

            if (existingJobOffer.isEmpty()) {
                jobOfferRepository.save(jobOffer);
                log.info("Saved new job offer: {}", jobOffer);
            } else {
                log.info("Job offer already exists, skipping: {}", jobOffer);
            }
        }
    }

    public static String parseDate(String dateTimeString) {
        Instant instant = Instant.parse(dateTimeString);
        LocalDate localDate = instant.atZone(ZoneId.of("UTC")).toLocalDate();
        return localDate.toString();
    }

    public List<JobOffer> getJobOffers(List<String> tech, List<String> seniority, List<String> location, LocalDate from, LocalDate to) {
        return jobOfferRepository.findJobOffers(tech, seniority, location, from, to);
    }
}