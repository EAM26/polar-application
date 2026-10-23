package com.em26.mapper;

import com.em26.dto.TrainingResponse;
import com.em26.model.Training;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TrainingMapper {

    public Training mapToEntity() {
        LocalDateTime createdAt = LocalDateTime.now();
        return Training.builder()
                .name(("Empty training " +  createdAt))
//                .name(checkName(request, createdAt))
                .createdAt(createdAt)
                .build();

    }

    public TrainingResponse mapToResponse(Training training) {
        return new TrainingResponse(
                training.getId(),
                training.getName(),
                training.getSportType(),
                training.getStartTime(),
                training.getStartDate(),
                training.getDurationSeconds(),
                training.getDistanceMeters(),
                training.getCreatedAt()
        );
    }
}
