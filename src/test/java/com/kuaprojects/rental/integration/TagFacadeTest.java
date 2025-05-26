package com.kuaprojects.rental.integration;

import com.kuaprojects.rental.tag.TagDetection;
import com.kuaprojects.rental.tag.TagDetectionFacade;
import com.kuaprojects.rental.tag.TagDetectionRepository;
import com.kuaprojects.rental.tag.TagVacantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        LocalDateTime end = LocalDateTime.of(2025, 5, 15, 23, 59);
        LocalDateTime dayEndTime = LocalDateTime.of(2025, 5, 15, 23, 59); // When device scanning hours have ended

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
        tagDetectionFacade.processDetections(start, end, dayEndTime);
        // Assert
        //Gets only three because of end null of the last tag
        var result = tagVacantRepository.findByTagCodeAndVacantFromBetween(tagCode, start, end);
        assertEquals(4, result.size());
    }

}
