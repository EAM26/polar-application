package com.em26.mapper;

import com.em26.dto.PhaseResponse;
import com.em26.dto.TrainingResponse;
import com.em26.model.Training;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TrainingMapper {

    private final PhaseMapper phaseMapper;

    public Training mapToEntity() {
        LocalDateTime createdAt = LocalDateTime.now();
        return Training.builder()
                .name(("Empty training " +  createdAt))
//                .name(checkName(request, createdAt))
                .createdAt(createdAt)
                .build();

    }

    public TrainingResponse mapToResponse(Training training) {
        List<PhaseResponse> phases = training.getPhaseList().stream()
                .map(phaseMapper::mapToResponse)
                .toList();
        return new TrainingResponse(
                training.getId(),
                training.getName(),
                training.getSportType(),
                training.getStartTime(),
                training.getStartDate(),
                training.getTotalDurationMeta(),
                training.getTotalDistanceMeta(),
                training.getAvgSpeedMeta(),
                training.getCaloriesMeta(),
                training.getAvgHeartRateMeta(),
                training.getCreatedAt(),
                phases
        );
    }

    public Training mapMetaDataToTraining(Map<String, String> metaData) {
        Training training = new Training();
        training.setName(metaData.get("Sport") + ": " + metaData.get("Date") + " " + metaData.get("Start time"));
        training.setSportType(metaData.get("Sport"));
        training.setStartTime(metaData.get("Start time"));
        training.setStartDate(metaData.get("Date"));
        training.setTotalDurationMeta(metaData.get("Duration"));
        training.setTotalDistanceMeta(metaData.get("Total distance (km)"));
        training.setAvgSpeedMeta(metaData.get("Average speed (km/h)"));
        training.setCaloriesMeta(metaData.get("Calories"));
        training.setAvgHeartRateMeta(metaData.get("Average heart rate (bpm)"));

        return training;
    }
}
