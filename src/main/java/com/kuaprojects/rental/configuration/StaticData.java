package com.kuaprojects.rental.configuration;

import com.kuaprojects.rental.tag.TagDetection;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class StaticData {

    public static List<TagDetection> getStaticTagDetections() {
        final String TAGCODE_1 = "TAG_1";
        final String TAGCODE_2 = "TAG_2";
        final String TAGCODE_3 = "TAG_3";

        List<TagDetection> input = new ArrayList<>();
        input.addAll(createDetections(TAGCODE_1));
        input.addAll(createDetections(TAGCODE_2));
        input.addAll(createDetections(TAGCODE_3));
        return input;
    }

    private static List<TagDetection> createDetections(String code) {
        var yesterday =  LocalDateTime.now().minusDays(1);
        final int currentYear = yesterday.getYear();
        final int currentMonth = yesterday.getMonthValue();
        final int currentDay = yesterday.getDayOfMonth();

        return List.of(
                new TagDetection(id(), code, LocalDateTime.of(currentYear, currentMonth, currentDay, 11, 15)),   // Same day
                new TagDetection(id(), code, LocalDateTime.of(currentYear, currentMonth, currentDay, 11, 20)),   // +5
                new TagDetection(id(), code, LocalDateTime.of(currentYear, currentMonth, currentDay, 11, 40)),   // +20
                new TagDetection(id(), code, LocalDateTime.of(currentYear, currentMonth, currentDay, 12, 0)),   // +20
                new TagDetection(id(), code, LocalDateTime.of(currentYear, currentMonth, currentDay, 12, 5)),   // +5 min
                new TagDetection(id(), code, LocalDateTime.of(currentYear, currentMonth, currentDay, 12, 8)),   // +3 min
                new TagDetection(id(), code, LocalDateTime.of(currentYear, currentMonth, currentDay, 12, 10)),  // +2 min
                new TagDetection(id(), code, LocalDateTime.of(currentYear, currentMonth, currentDay, 12, 12)),  // +2 min
                new TagDetection(id(), code, LocalDateTime.of(currentYear, currentMonth, currentDay, 12, 15)),  // +3 min
                new TagDetection(id(), code, LocalDateTime.of(currentYear, currentMonth, currentDay, 12, 17)),  // +2 min
                new TagDetection(id(), code, LocalDateTime.of(currentYear, currentMonth, currentDay, 12, 19)),  // +2 min
                new TagDetection(id(), code, LocalDateTime.of(currentYear, currentMonth, currentDay, 12, 35)),  // +16 min → gap > 10 min
                new TagDetection(id(), code, LocalDateTime.of(currentYear, currentMonth, currentDay, 13, 35)),  // + 1h
                new TagDetection(id(), code, LocalDateTime.of(currentYear, currentMonth, currentDay, 15, 35)),  // + 2h
                new TagDetection(id(), code, LocalDateTime.of(currentYear, currentMonth, currentDay, 15, 36)),
                new TagDetection(id(), code, LocalDateTime.of(currentYear, currentMonth, currentDay, 17, 00)),
                new TagDetection(id(), code, LocalDateTime.of(currentYear, currentMonth, currentDay, 19, 24)),
                new TagDetection(id(), code, LocalDateTime.of(currentYear, currentMonth, currentDay, 20, 24)),
                new TagDetection(id(), code, LocalDateTime.of(currentYear, currentMonth, currentDay, 20, 27)),
                new TagDetection(id(), code, LocalDateTime.of(currentYear, currentMonth, currentDay, 20, 28)),
                new TagDetection(id(), code, LocalDateTime.of(currentYear, currentMonth, currentDay, 20, 30)),
                new TagDetection(id(), code, LocalDateTime.of(currentYear, currentMonth, currentDay, 21, 00))
        );
    }

    private static long id() {
        return Math.abs(ThreadLocalRandom.current().nextLong());
    }
}
