package com.kuaprojects.rental.frontend.utils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Style;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Utils {
    public static void contentStyling(Div content) {
        content.setWidth("100%");
        content.getStyle().setDisplay(Style.Display.FLEX);
        content.getStyle().setFlexWrap(Style.FlexWrap.WRAP);
    }

    public static DateTimeRange getRange(String option) {
        LocalDate today = LocalDate.now();
        switch (option) {
//            case "Yesterday":
            case "Vakar":
                return new DateTimeRange(
                        LocalDateTime.of(today.minusDays(1), LocalTime.MIN),
                        LocalDateTime.of(today.minusDays(1), LocalTime.MAX)
                );
//            case "Day Before Yesterday":
            case "Užvakar":
                return new DateTimeRange(
                        LocalDateTime.of(today.minusDays(2), LocalTime.MIN),
                        LocalDateTime.of(today.minusDays(2), LocalTime.MAX)
                );
//            case "This week":
            case "Ši savaitė":
                return new DateTimeRange(
                        LocalDateTime.of(today.with(java.time.DayOfWeek.MONDAY), LocalTime.MIN),
                        LocalDateTime.of(today, LocalTime.MAX)
                );
//            case "Last Week":
            case "Praėjusi savaitė":
                LocalDate startOfWeek = today.minusWeeks(1).with(java.time.DayOfWeek.MONDAY);
                LocalDate endOfWeek = startOfWeek.plusDays(6);
                return new DateTimeRange(
                        LocalDateTime.of(startOfWeek, LocalTime.MIN),
                        LocalDateTime.of(endOfWeek, LocalTime.MAX)
                );
            default:
                return null;
        }
    }
    // Helper class to hold from/to datetimes
    public static class DateTimeRange {
        private final LocalDateTime from;
        private final LocalDateTime to;

        public DateTimeRange(LocalDateTime from, LocalDateTime to) {
            this.from = from;
            this.to = to;
        }

        public LocalDateTime getFrom() {
            return from;
        }

        public LocalDateTime getTo() {
            return to;
        }

        @Override
        public String toString() {
            return "From: " + from.format(DateTimeFormatter.ISO_DATE) + ", To: " + to.format(DateTimeFormatter.ISO_DATE);
        }
    }

    public static String generateRandomHexColor() {
        Random random = new Random();
        int color = random.nextInt(0x1000000); // 0x1000000 = 16777216
        return String.format("#%06X", color); // Uppercase hex
    }

    public static HorizontalLayout createButtonLayout(Component... children) {
        var layout = new HorizontalLayout(children);
        layout.getStyle()
                .set("border", "1px solid #ccc")
                .set("background-color", "#f9f9f9")
                .set("padding", "10px")
                .set("border-radius", "5px")
                .set("gap", "10px");
        return layout;
    }
}
