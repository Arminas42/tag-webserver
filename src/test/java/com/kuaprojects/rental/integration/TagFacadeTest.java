package com.kuaprojects.rental.integration;

import com.kuaprojects.rental.tag.*;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TagFacadeTest {

    @Autowired
    private TagDetectionRepository tagDetectionRepository;
    @Autowired
    private TagDetectionFacade tagDetectionFacade;
    @Autowired
    private TagVacantRepository tagVacantRepository;

//    @InjectMocks
//    private TagDetectionService tagDetectionService;

    @Test
    public void testProcessTags() {
        // Arrange
        String tagCode = "TAG_1";
        LocalDateTime start = LocalDateTime.of(2025, 5, 15, 0, 0);
        LocalDateTime endScanningTime = LocalDateTime.of(2025, 5, 15, 23, 59);
        LocalDateTime businessDeadline = LocalDateTime.of(2025, 5, 15, 23, 59); // When device scanning hours have ended

        List<TagDetection> input = List.of(
                new TagDetection(1L, tagCode, LocalDateTime.of(2025, 5, 15, 12, 0)),   // Same day
                new TagDetection(2L, tagCode, LocalDateTime.of(2025, 5, 15, 12, 5)),   // +5 min
                new TagDetection(3L, tagCode, LocalDateTime.of(2025, 5, 15, 12, 8)),   // +3 min
                new TagDetection(4L, tagCode, LocalDateTime.of(2025, 5, 15, 12, 10)),  // +2 min
                new TagDetection(5L, tagCode, LocalDateTime.of(2025, 5, 15, 12, 12)),  // +2 min
                new TagDetection(6L, tagCode, LocalDateTime.of(2025, 5, 15, 12, 15)),  // +3 min
                new TagDetection(7L, tagCode, LocalDateTime.of(2025, 5, 15, 12, 17)),  // +2 min
                new TagDetection(8L, tagCode, LocalDateTime.of(2025, 5, 15, 12, 19)),  // +2 min
                new TagDetection(9L, tagCode, LocalDateTime.of(2025, 5, 15, 12, 35)),  // +16 min → gap > 10 min
                new TagDetection(10L, tagCode, LocalDateTime.of(2025, 5, 15, 13, 35)),  // + 1h
                new TagDetection(11L, tagCode, LocalDateTime.of(2025, 5, 15, 15, 35)),  // + 2h
                new TagDetection(12L, tagCode, LocalDateTime.of(2025, 5, 15, 15, 36)) // gone till end of day
                 );

//        when(tagDetectionRepository.findByDetectionTimeBetween(start, end)).thenReturn(mockResult);
        // Setup
        for (TagDetection tagDetection:input) {
            tagDetectionRepository.save(tagDetection);
        }
        // Act
        tagDetectionFacade.processDetections(start, endScanningTime, businessDeadline);
        // Assert
        //Gets only three because of end null of the last tag
        var result = tagVacantRepository.findByTagCodeAndVacantToBetween(tagCode, start, endScanningTime);
        assertEquals(4, result.size());
    }
    @Test
    public void testEndingOfOngoingVacancy_onlyOneDetection() {
        // Arrange
        String tagCode = "TAG_2";
        LocalDateTime dayBeforeStart = LocalDateTime.of(2025, 6, 14, 0, 0);
        LocalDateTime start = LocalDateTime.of(2025, 6, 15, 0, 0);
        LocalDateTime scanningEndTime = LocalDateTime.of(2025, 6, 15, 23, 59);
        LocalDateTime businessDeadline = LocalDateTime.of(2025, 6, 15, 20, 00); // When device scanning hours have ended

        List<TagDetection> input = List.of(
                new TagDetection(1L, tagCode, LocalDateTime.of(2025, 6, 15, 12, 0))
        );

//        when(tagDetectionRepository.findByDetectionTimeBetween(start, end)).thenReturn(mockResult);
        // Setup
        for (TagDetection tagDetection:input) {
            tagDetectionRepository.save(tagDetection);
        }
        tagVacantRepository.save(TagVacant.builder()
                        .tagCode(tagCode)
                        .status(ProcessingStatus.STILL_VACANT)
                        .vacantFrom(dayBeforeStart)
                        .vacantTo(null)
                .build());
        // Act
        tagDetectionFacade.processDetections(start, scanningEndTime, businessDeadline);
        // Assert
        var result = tagVacantRepository.findByTagCodeAndVacantToBetween(tagCode, start, businessDeadline);
        var firstVacancy = result.getFirst();
        assertEquals(1, result.size());
        assertEquals(ProcessingStatus.ENDED, firstVacancy.getStatus());
        assertTrue(ObjectUtils.isNotEmpty(firstVacancy.getVacantTo()));
        assertEquals(LocalDateTime.of(2025, 6, 15, 12, 0), firstVacancy.getVacantTo());
        // Check if STILL_VACANT is saved
        var resultStillVacant = tagVacantRepository.findByTagCodeAndVacantFromBetween(tagCode, start, businessDeadline);
        var stillVacant = resultStillVacant.getFirst();
        assertEquals(1, resultStillVacant.size());
        assertEquals(ProcessingStatus.STILL_VACANT, stillVacant.getStatus());
    }

    @Test
    public void testProcessTags_edgeCases() {
        // Arrange
        String tagCode = "TAG_3";
        LocalDateTime start = LocalDateTime.of(2025, 7, 15, 8, 0);
        LocalDateTime dayBeforeStart = start.minusDays(1);
        LocalDateTime businessDeadline = LocalDateTime.of(2025, 7, 15, 20, 00);
        LocalDateTime endScanningTime = LocalDateTime.of(2025, 7, 15, 23, 59);

        LocalDateTime tagDetectionTime = LocalDateTime.of(2025, 7, 15, 12, 0);

        List<TagDetection> input = List.of(
                new TagDetection(1L, tagCode, tagDetectionTime),
                new TagDetection(2L, tagCode, tagDetectionTime.plusMinutes(5)),
                new TagDetection(4L, tagCode, tagDetectionTime.plusMinutes(16)),
                new TagDetection(9L, tagCode, tagDetectionTime.plusMinutes(18)),
                new TagDetection(10L, tagCode, tagDetectionTime.plusMinutes(60)),
                new TagDetection(11L, tagCode, tagDetectionTime.plusMinutes(120)),
                new TagDetection(12L, tagCode, tagDetectionTime.plusMinutes(130)),
                new TagDetection(13L, tagCode, businessDeadline.minusMinutes(1)),
                new TagDetection(14L, tagCode, businessDeadline.plusMinutes(10)), // detections after business deadline should not be considered
                new TagDetection(15L, tagCode, businessDeadline.plusMinutes(50)) // detections after business deadline should not be considered
        );

//        when(tagDetectionRepository.findByDetectionTimeBetween(start, end)).thenReturn(mockResult);
        // Setup
        for (TagDetection tagDetection:input) {
            tagDetectionRepository.save(tagDetection);
        }
        tagVacantRepository.save(TagVacant.builder()
                .id(16L)
                .tagCode(tagCode)
                .status(ProcessingStatus.STILL_VACANT)
                .vacantFrom(dayBeforeStart)
                .vacantTo(null)
                .build());
        // Act
        tagDetectionFacade.processDetections(start, endScanningTime, businessDeadline);
        // Assert
        //Gets only three because of end null of the last tag
        var result = tagVacantRepository.findByTagCodeAndVacantToBetween(tagCode, start, endScanningTime);
        assertEquals(6, result.size());
    }
}
