package com.em26.model;

import lombok.Data;

@Data
public class AnalysisBlock {

    private String description;
    private Long phaseId;
    private int sequenceNumber;
    private int duration;
    private int startTime;
    private int stopTime;
    private Double hrAvg;
    private Double cadenceAvg;
    private Double powerAvg;
    private Double speedAvg;
    private Double paceAvg;
    private Double distance;
    private Integer startHr;
    private Integer stopHr;
    private Integer hrMax;
    private Integer hrMin;
    private Integer ascent;
    private Integer descent;
}

