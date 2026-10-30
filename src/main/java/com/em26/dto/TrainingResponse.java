package com.em26.dto;

import java.time.LocalDateTime;
import java.util.List;

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
        String avgHeartRateMeta,
        LocalDateTime createdAt,
        List<PhaseResponse> phases
) {
}
