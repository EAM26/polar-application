package com.em26.dto;

public record PhaseResponse(
        Long id,
        String name,
        int duration,
        int startTime,
        int stopTime,
        Double hrAvg,
        Double cadenceAvg,
        Double powerAvg,
        Double distance,
        Double speedAvg,
        Double paceAvg,
        Long trainingId
) {
}
