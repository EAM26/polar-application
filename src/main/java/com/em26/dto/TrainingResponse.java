package com.em26.dto;

import java.time.LocalDateTime;

public record TrainingResponse(
        Long id,
        String name,
        String sportType,
        String startTime,
        String startDate,
        String totalDurationMeta,
        String totalDistanceMeta,
        String avgSpeedMeta,
        String caloriesMeta,
        LocalDateTime createdAt
) {
}
