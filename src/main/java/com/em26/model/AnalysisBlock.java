package com.em26.model;

import lombok.Data;

@Data
public class AnalysisBlock {

    private Long trainingId;
    private Long phaseId;
    private int sequenceNumber;
    private int initialDuration;
    private int actualDuration;
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

// Todo:
// Service: check if training had phases, if not see training as one phase but dont create one
// set initial duration -> calculate actual by Phase duration or whole training
// set and calculate the rest similar to Phase methodology

