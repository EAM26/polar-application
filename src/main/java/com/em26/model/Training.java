package com.em26.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String sportType;
    private LocalTime startTime;
    private LocalDate startDate;

    private Integer durationSeconds;
    private Integer distanceMeters;

    @CreationTimestamp
    private LocalDateTime createdAt;








}
