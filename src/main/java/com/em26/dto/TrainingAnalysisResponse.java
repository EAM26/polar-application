package com.em26.dto;

import com.em26.model.AnalysisBlock;

import java.util.List;

public record TrainingAnalysisResponse(
        Long trainingId,
        String nameTraining,
        String totalDurationMeta,
        String totalDistanceMeta,
        String avgSpeedMeta,
        String caloriesMeta,
        String avgHeartRateMeta,
        String workoutDescription,
        String rpe,
        String comment,

        List<AnalysisBlock> blocks
) {
}
