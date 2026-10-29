package com.em26.mapper;

import com.em26.dto.PhaseRequest;
import com.em26.dto.PhaseResponse;
import com.em26.model.Phase;
import com.em26.model.Training;
import org.springframework.stereotype.Component;

@Component
public class PhaseMapper {

    public Phase mapToEntity(Training training, PhaseRequest request) {
        return Phase.builder()
                .name(request.name())
                .duration(request.duration())
                .training(training)
                .build();
    }

    public PhaseResponse mapToResponse(Phase phase) {
        return new PhaseResponse(
                phase.getId(),
                phase.getName(),
                phase.getDuration(),
                phase.getStartTime(),
                phase.getEndTime(),
                phase.getTraining().getId()
        );
    }
}
