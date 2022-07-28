package com.example.moview.moview.util.datetime;

import com.example.moview.moview.exception.NullParameterException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Component
public class DateTimeConverterImpl implements DateTimeConverter {

    private final String DATE_PATTERN = "dd.MM.yyyy";
    private final String DATE_TIME_PATTERN = "dd.MM.yyyy HH:mm:ss";

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
        if (Objects.isNull(dateTimeString)) {
            throw new NullParameterException(String.format("The parameter cannot be null in the parse method of %s.",
                    this.getClass().getName()));
        }

        return dateTimeString.equalsIgnoreCase("null") ? null
                : LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
    }

    public String formatLocalDateTimeToString(final LocalDateTime dateTime) {
        return Objects.nonNull(dateTime) ? dateTime.format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)) : null;
    }
}
