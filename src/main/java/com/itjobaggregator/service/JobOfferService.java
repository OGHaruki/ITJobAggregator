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

    @Autowired
    private RequiredSkillsRepository requiredSkillsRepository;

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
        String justJoinItResponse = fetchJustJoinItJobOffers(justJoinItUrl);

        String noFluffJobsUrl = "https://nofluffjobs.com/api/joboffers/main?pageTo=2&pageSize=20&withSalaryMatch=true&salaryCurrency=PLN&salaryPeriod=month&region=pl&language=pl-PL";
        String noFluffJobsResponse = fetchNoFluffJobOffers(noFluffJobsUrl);

        // Parse and save job offers to database
        if (justJoinItResponse != null) {
            List<JobOffer> jobOfferList = parseJustJoinItResponse(justJoinItResponse);
            jobOfferRepository.saveAll(jobOfferList);
            //jobOfferList.forEach(System.out::println);
        }

        if (noFluffJobsResponse != null) {
            List<JobOffer> jobOfferList = parseNoFluffResponse(noFluffJobsResponse);
            jobOfferRepository.saveAll(jobOfferList);
            //jobOfferList.forEach(System.out::println);
        }
    }

    private String fetchJustJoinItJobOffers(String url) {
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

        HttpResponse<String> response;

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() != 200) {
                System.out.println("Failed to fetch job offers from JustJoinIT API. HTTP status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            Logger.getLogger(JobOfferService.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return response.body();
    }

    private String fetchNoFluffJobOffers(String url) {
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

        HttpResponse<String> response;

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() != 200) {
                System.out.println("Failed to fetch job offers from NoFluffJobs API. HTTP status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            Logger.getLogger(JobOfferService.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return response.body();
    }

    private void saveSkillsToDatabase(JobOffer jobOffer, List<String> requiredSkills) {
        for (String skill : requiredSkills) {
            Optional<RequiredSkills> existingSkill = requiredSkillsRepository.findRequiredSkillsByName(skill);
            if(existingSkill.isPresent()) {
                jobOffer.addRequiredSkill(existingSkill.get());
            } else {
                RequiredSkills newSkill = new RequiredSkills();
                newSkill.setName(skill);
                requiredSkillsRepository.save(newSkill);
                jobOffer.addRequiredSkill(newSkill);
            }
        }
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
            //1jobOffer.setRawData(jobNode.toString());
            jobOffer.setSource(JobSource.JustJoinIT);

            List<String> requiredSkills = new ArrayList<>();
            if (jobNode.has("requiredSkills")) {
                for (JsonNode skillNode : jobNode.get("requiredSkills")) {
                    requiredSkills.add(skillNode.asText());
                }
            }

            saveSkillsToDatabase(jobOffer, requiredSkills);

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

    private List<JobOffer> parseNoFluffResponse(String noFluffJobsResponse) throws JsonProcessingException {

        JsonNode rootNode = objectMapper.readTree(noFluffJobsResponse);
        JsonNode jobOffersNode = rootNode.get("postings");
        List<JobOffer> jobOfferList = new ArrayList<>();

        for (JsonNode jobNode : jobOffersNode) {
            JobOffer jobOffer = new JobOffer();
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
            saveSkillsToDatabase(jobOffer, requiredSkills);
        }
    }
}
