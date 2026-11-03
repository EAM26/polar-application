package com.em26.mapper;

import com.em26.dto.TrainingAnalysisResponse;
import com.em26.model.AnalysisBlock;
import com.em26.model.Training;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AnalysisMapper {

    public TrainingAnalysisResponse mapToResponse(Training training, List<AnalysisBlock> blocks) {
        return new TrainingAnalysisResponse(
                training.getId(),
                training.getName(),
                training.getTotalDurationMeta(),
                training.getTotalDistanceMeta(),
                training.getAvgSpeedMeta(),
                training.getCaloriesMeta(),
                training.getAvgHeartRateMeta(),
                training.getWorkoutDescription(),
                training.getRpe(),
                training.getComment(),
                blocks
        );
    }
}
