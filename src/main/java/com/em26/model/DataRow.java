package com.em26.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class DataRow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String timeLabel;
    private int indexSeconds;
    private Integer heartRate;
    private Integer cadence;
    private Integer altitude;
    private Integer stride;
    private Double distance;
    private Double temperature;
    private Integer power;

    @ManyToOne
    @JoinColumn(name = "training", nullable = false)
    private Training training;
}
