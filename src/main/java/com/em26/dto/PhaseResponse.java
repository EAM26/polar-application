package com.em26.dto;

public record PhaseResponse(
        Long id,
        String name,
        int duration,
        int startTime,
        int stopTime,
//        todo: refactor hrAvg to general analysis entity with other Avg's
        Double hrAvg,
        Long trainingId
) {
}
