package com.itjobaggregator.service;

import com.itjobaggregator.model.JobOffer;
import com.itjobaggregator.model.RequiredSkills;
import com.itjobaggregator.repository.RequiredSkillsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequiredSkillService {

    @Autowired
    private RequiredSkillsRepository requiredSkillsRepository;

    @Transactional
    public void saveSkillsToDatabase(JobOffer jobOffer, List<String> requiredSkills) {
        for (String skill : requiredSkills) {
            Optional<RequiredSkills> existingSkill = Optional.of(requiredSkillsRepository.findRequiredSkillsByName(skill)
                    .orElseGet(() -> {
                        RequiredSkills newSkill = new RequiredSkills();
                        newSkill.setName(skill);
                        return requiredSkillsRepository.save(newSkill);
                    }));

            existingSkill.ifPresent(jobOffer::addRequiredSkill);
        }
    }

}
