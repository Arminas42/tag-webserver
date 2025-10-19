package com.kuaprojects.rental.tag;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TagDetectionFacade {

    private final TagDetectionRepository tagDetectionRepository;
    private final TagVacantRepository tagVacantRepository;

    public TagDetectionFacade(TagDetectionRepository tagDetectionRepository, TagVacantRepository tagVacantRepository) {
        this.tagDetectionRepository = tagDetectionRepository;
        this.tagVacantRepository = tagVacantRepository;
    }

    public void processDetections(LocalDateTime start, LocalDateTime scanningEndDateTime, LocalDateTime deadline) {
        var detections =  tagDetectionRepository.findByDetectionTimeBetween(start, scanningEndDateTime);
        Map<String, List<TagDetection>> groupedDetections = detections.stream().collect(Collectors.groupingBy(TagDetection::getTagCode));
        List<TagVacant> vacantTags = createTagVacantToSave(scanningEndDateTime, deadline, groupedDetections);
        tagVacantRepository.saveAll(vacantTags);
        var softDeletedDetections = detections.stream().peek(x -> x.setSoftDelete(true)).toList();
        tagDetectionRepository.saveAll(softDeletedDetections);
    }

    /**
     * @param scanningEnd Variable to indicate actual hardware checking  of detections, ought to be equal or bigger than deadline
     * @param deadline variable to indicate by what time are we interested in checking for detected tags
     * @param groupedDetections detection objects grouped by time window
     * @return List<TagVacant> objects to be saved to database
     */
    public List<TagVacant> createTagVacantToSave(LocalDateTime scanningEnd, LocalDateTime deadline, Map<String, List<TagDetection>> groupedDetections) {
        List<TagVacant> vacantTags = new ArrayList<>();
        var unfinishedVacancies = tagVacantRepository.findByStatus(ProcessingStatus.STILL_VACANT).stream().collect(
                Collectors.toMap(
                    TagVacant::getTagCode,
                    tagVacant -> tagVacant,
                    (element1, element2) -> {throw new RuntimeException("tagVacant object unimplemented logic");}
                )
        );

        var actualDeadline = ObjectUtils.isNotEmpty(deadline) ? deadline : scanningEnd;
        for (Map.Entry<String, List<TagDetection>> entry : groupedDetections.entrySet()) {
            String tagCode = entry.getKey();
            int allDetectionSize = entry.getValue().size();
            int processingStartIndex = 1;
            List<TagDetection> detectionsToProcess = new ArrayList<>(entry.getValue()
                    .stream()
                    .sorted(Comparator.comparing(TagDetection::getDetectionTime))
                    .filter(x -> x.getDetectionTime().isBefore(deadline))
                    .toList());

            //Check for unfinished vacancies:
            var unfinishedVacancy = unfinishedVacancies.get(tagCode);
            if(ObjectUtils.isNotEmpty(unfinishedVacancy)){
                var first_detection = detectionsToProcess.getFirst();
                unfinishedVacancy.setVacantTo(first_detection.getDetectionTime());
                unfinishedVacancy.setStatus(ProcessingStatus.ENDED);
                vacantTags.add(unfinishedVacancy);

            }
            //

            for (int i = processingStartIndex; i < detectionsToProcess.size(); i++) {
                LocalDateTime previousTime = detectionsToProcess.get(i - 1).getDetectionTime();
                LocalDateTime currentTime = detectionsToProcess.get(i).getDetectionTime();
                var shouldSave = Duration.between(previousTime, currentTime).toMinutes() >= 10 &&
                        currentTime.isBefore(scanningEnd);
                if (shouldSave) {
                    vacantTags.add(TagVacant.builder()
                            .status(ProcessingStatus.ENDED)
                            .tagCode(tagCode)
                            .vacantFrom(previousTime)
                            .vacantTo(currentTime)
                            .build());
                }
            }
            // Assuming all tags should be monitored after deadline
            TagDetection lastDetection = detectionsToProcess.getLast();
            if (allDetectionSize == detectionsToProcess.size()) {
                vacantTags.add(TagVacant.builder()
                        .status(ProcessingStatus.STILL_VACANT)
                        .tagCode(tagCode)
                        .vacantFrom(lastDetection.getDetectionTime())
                        .vacantTo(null)
                        .build());
            }
        }
        return vacantTags;
    }

}
