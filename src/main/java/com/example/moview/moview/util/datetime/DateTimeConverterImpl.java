package com.example.moview.moview.util.datetime;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Component
public class DateTimeConverterImpl implements DateTimeConverter {

    private static final String DATE_PATTERN = "dd.MM.yyyy";
    private static final String DATE_TIME_PATTERN = "dd.MM.yyyy HH:mm:ss";

    public Duration parseDuration(final String durationString) {
        return Duration.between(LocalTime.MIN, LocalTime.parse(durationString));
    }

    public String formatDurationToString(final Duration duration) {
        return String.format("%02d:%02d:%02d", duration.toHours(), duration.toMinutesPart(), duration.toSecondsPart());
    }

    public LocalDate parseLocalDate(final String dateString) {
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    public String formatLocalDateToString(final LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    public LocalDateTime parseLocalDateTime(final String dateTimeString) {
        Objects.requireNonNull(dateTimeString);
        return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
    }

    public String formatLocalDateTimeToString(final LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
    }
}