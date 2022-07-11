package com.example.moview.moview.util.datetime;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    private static final String DATE_PATTERN = "dd.MM.yyyy";
    private static final String DATE_TIME_PATTERN = "dd.MM.yyyy HH:mm:ss";

    private DateTimeUtil() {
        throw new UnsupportedOperationException(String.format("Attempt to create %S", this.getClass().getSimpleName()));
    }

    public static Duration parseDuration(final String durationString) {
        final LocalTime time = LocalTime.parse(durationString);
        return Duration.between(LocalTime.MIN, time);
    }

    public static LocalDate parseDate(final String dateString) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        return LocalDate.parse(dateString, formatter);
    }

    public static String formatLocalDateToString(final LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    public static String formatDurationToString(final Duration duration) {
        final long hh = duration.toHours();
        final long mm = duration.toMinutesPart();
        final long ss = duration.toSecondsPart();
        return String.format("%02d:%02d:%02d", hh, mm, ss);
    }

    public static String formatLocalDateTimeToString(final LocalDateTime dateTime) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
        return dateTime.format(formatter);
    }
}
