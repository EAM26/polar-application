package com.em26.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String sportType;
    private String startTime;
    private String startDate;
    private String totalDurationMeta;
    private String totalDistanceMeta;
    private String avgSpeedMeta;
    private String caloriesMeta;
    private String avgHeartRateMeta;

    @CreationTimestamp
    private LocalDateTime createdAt;








}
