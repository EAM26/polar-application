package com.em26.dto;

public record TrainingRequest(
        String workoutDescription,
        String rpe,
        String comment
) {
}
