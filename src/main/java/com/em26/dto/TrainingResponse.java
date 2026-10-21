package com.em26.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record TrainingResponse(
        Long id,
        String name,
        String sportType,
        LocalTime startTime,
        LocalDate startDate,
        Integer durationSeconds,
        Integer distanceMeters,
        LocalDateTime createdAt
) {
}
