package com.em26.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Phase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
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


    @ManyToOne
    @JoinColumn(name = "trainingId", nullable = false)
    private Training training;
}
