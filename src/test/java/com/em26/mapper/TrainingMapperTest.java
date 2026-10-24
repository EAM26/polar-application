package com.em26.mapper;

import com.em26.dto.TrainingResponse;
import com.em26.model.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class TrainingMapperTest {

    private TrainingMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new TrainingMapper();
    }

    @Test
    void mapToEntity_createsEmptyTraining() {
//        Given

//        When
        Training result = mapper.mapToEntity();

//        Then
        assertThat(result.getId()).isNull();
        assertThat(result.getName()).isEqualTo("Empty training " + result.getCreatedAt());
        assertThat(result.getSportType()).isNull();
        assertThat(result.getStartDate()).isNull();
        assertThat(result.getStartTime()).isNull();
        assertThat(result.getDistanceMeters()).isNull();
        assertThat(result.getDurationSeconds()).isNull();
        assertThat(result.getCreatedAt()).isNotNull();

    }

    @Test
    void mapToResponseShouldReturnValidResponse() {
//        Given
        LocalDateTime createdAt = LocalDateTime.parse("2026-10-24T10:01:00.123456");
        Training training = Training.builder()
                .id(10L)
                .name("RUNNING 2026-05-01 12:00:01")
                .sportType("RUNNING")
                .startDate(LocalDate.of(2026, 5, 1))
                .startTime(LocalTime.of(12, 0, 1))
                .distanceMeters(4205)
                .durationSeconds(1200)
                .createdAt(createdAt)
                .build();

//        When
        TrainingResponse response = mapper.mapToResponse(training);

//        Then
        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.name()).isEqualTo("RUNNING 2026-05-01 12:00:01");
        assertThat(response.sportType()).isEqualTo("RUNNING");
        assertThat(response.startDate()).isEqualTo(LocalDate.of(2026, 5, 1));
        assertThat(response.startTime()).isEqualTo(LocalTime.of(12, 0, 1));
        assertThat(response.distanceMeters()).isEqualTo(4205);
        assertThat(response.durationSeconds()).isEqualTo(1200);
        assertThat(response.createdAt()).isEqualTo(createdAt);
    }
}