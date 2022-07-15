package com.example.moview.moview.util.datetime;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface DateTimeConverter {

    Duration parseDuration(String durationString);

    String formatDurationToString(Duration duration);

    LocalDate parseLocalDate(String dateString);

    String formatLocalDateToString(LocalDate date);

    LocalDateTime parseLocalDateTime(String dateTimeString);

    String formatLocalDateTimeToString(LocalDateTime dateTime);
}
