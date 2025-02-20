package com.fitness.fitness_tracker.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * https://thorben-janssen.com/jpa-tips-map-duration-attribute/
 */
@Converter(autoApply = true)
public class DurationConverter implements AttributeConverter<Duration, Long> {

    public DurationConverter() {

    }

    @Override
    public Long convertToDatabaseColumn(Duration attribute) {
        return attribute.toMillis();
    }

    @Override
    public Duration convertToEntityAttribute(Long duration) {
        return Duration.of(duration, ChronoUnit.MILLIS);
    }
}