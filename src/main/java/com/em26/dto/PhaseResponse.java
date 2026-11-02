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
        Double speedAvg,
        Double paceAvg,
        Double distance,
        Integer startHr,
        Integer stopHr,
        Long trainingId
) {
}
