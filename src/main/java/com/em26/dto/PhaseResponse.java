package com.em26.dto;

public record PhaseResponse(
        Long id,
        String name,
        int duration,
        int startTime,
        int stopTime,
        Double hrAvg,
        Long trainingId
) {
}
