package com.kuaprojects.rental.tag;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TagDetectionFacade {

    private final TagDetectionRepository tagDetectionRepository;
    private final TagVacantRepository tagVacantRepository;

    public TagDetectionFacade(TagDetectionRepository tagDetectionRepository, TagVacantRepository tagVacantRepository) {
        this.tagDetectionRepository = tagDetectionRepository;
        this.tagVacantRepository = tagVacantRepository;
    }

    //Todo extract logic
    public void processDetections(LocalDateTime start, LocalDateTime end, LocalDateTime scanningEndDateTime) {

        Map<String, List<TagDetection>> groupedDetections = tagDetectionRepository
                .findByDetectionTimeBetween(start, end)
                .stream()
                .collect(Collectors.groupingBy(TagDetection::getTagCode));
        List<TagVacant> vacantTags = createTagVacantToSave(scanningEndDateTime, groupedDetections);
        for (TagVacant entityToSave : vacantTags) {
            tagVacantRepository.save(entityToSave);
        }
        //TODO: delete TagDetections

    }

    public List<TagVacant> createTagVacantToSave(LocalDateTime scanningEndDateTime, Map<String, List<TagDetection>> groupedDetections) {
        List<TagVacant> vacantTags = new ArrayList<>();
        for (Map.Entry<String, List<TagDetection>> entry : groupedDetections.entrySet()) {
            String tagCode = entry.getKey();
            List<TagDetection> detections = entry.getValue()
                    .stream()
                    .sorted(Comparator.comparing(TagDetection::getDetectionTime))
                    .collect(Collectors.toList());

            for (int i = 1; i < detections.size(); i++) {
                LocalDateTime previousTime = detections.get(i - 1).getDetectionTime();
                LocalDateTime currentTime = detections.get(i).getDetectionTime();
                var shouldSave = Duration.between(previousTime, currentTime).toMinutes() > 10 &&
                        currentTime.isBefore(scanningEndDateTime);
                if (shouldSave) {
                    vacantTags.add(TagVacant.builder()
                            .status(ProcessingStatus.ENDED) // Example; adjust as needed
                            .tagCode(tagCode) // Placeholder, adjust mapping
                            .vacantFrom(previousTime)
                            .vacantTo(currentTime)
                            .build());
                }
            }

            // Handle last detection if it's more than 10 minutes ago from day end
            TagDetection lastDetection = detections.get(detections.size() - 1);
            if (Duration.between(lastDetection.getDetectionTime(), scanningEndDateTime).toMinutes() > 10) {
                vacantTags.add(TagVacant.builder()
                        .status(ProcessingStatus.STILL_VACANT) // Example; adjust as needed
                        .tagCode(tagCode) // Placeholder, adjust mapping
                        .vacantFrom(lastDetection.getDetectionTime())
                        .vacantTo(null)
                        .build());
            }
        }
        return vacantTags;
    }

}
