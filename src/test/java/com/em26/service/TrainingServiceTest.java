package com.em26.service;

import com.em26.dto.TrainingResponse;
import com.em26.exception.RecordNotFoundException;
import com.em26.mapper.TrainingMapper;
import com.em26.model.Training;
import com.em26.repository.TrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TrainingMapper trainingMapper;

    @InjectMocks
    private TrainingService trainingService;

    private Training trainingEmptyBeforeSaved;
    private Training trainingEmptySaved;
    private Training trainingWithDataSaved;

    private TrainingResponse responseEmpty;
    private TrainingResponse responseWithData;

    private Long idSavedTraining;
    private Long missingId;

    @BeforeEach
    void setUp() {
        LocalDateTime createdAt = LocalDateTime.now();
        idSavedTraining = 10L;
        missingId = 90L;

        trainingEmptyBeforeSaved = Training.builder()
                .name(("Empty training " + createdAt))
                .createdAt(createdAt)
                .build();

        trainingEmptySaved = Training.builder()
                .id(1L)
                .name(trainingEmptyBeforeSaved.getName())
                .createdAt(createdAt)
                .build();

        responseEmpty = new TrainingResponse(
                1L,
                trainingEmptySaved.getName(),
                null,
                null,
                null,
                null,
                null,
                createdAt
        );

        trainingWithDataSaved = Training.builder()
                .id(10L)
                .name("RUNNING 2026-05-01 12:00:01")
                .sportType("RUNNING")
                .startTime(LocalTime.of(12, 0, 1))
                .startDate(LocalDate.of(2026, 5, 1))
                .distanceMeters(4205)
                .durationSeconds(1200)
                .createdAt(createdAt)
                .build();

        responseWithData = new TrainingResponse(
                10L,
                "RUNNING 2026-05-01 12:00:01",
                "RUNNING",
                LocalTime.of(12, 0, 1),
                LocalDate.of(2026, 5, 1),
                1200,
                4205,
                createdAt
        );
    }

    @Test
    void createEmptyTrainingShouldReturnEmptyRepo() {

//        Given
        when(trainingMapper.mapToEntity()).thenReturn(trainingEmptyBeforeSaved);
        when(trainingRepository.save(trainingEmptyBeforeSaved)).thenReturn(trainingEmptySaved);
        when(trainingMapper.mapToResponse(trainingEmptySaved)).thenReturn(responseEmpty);

//        When
        TrainingResponse result = trainingService.createTraining();

//        Then
        assertThat(result).isSameAs(responseEmpty);
        verify(trainingRepository).save(trainingEmptyBeforeSaved);
    }


    @Test
    void getAllTrainingsShouldReturnListOfTwoTrainings() {
//        Given
        when(trainingRepository.findAll()).thenReturn(List.of(trainingEmptySaved, trainingWithDataSaved));
        when(trainingMapper.mapToResponse(trainingEmptySaved)).thenReturn(responseEmpty);
        when(trainingMapper.mapToResponse(trainingWithDataSaved)).thenReturn(responseWithData);

//        When
        List<TrainingResponse> results = trainingService.getAllTrainings();

//        Then
        assertThat(results).containsExactlyInAnyOrder(responseEmpty, responseWithData);
    }

    @Test
    void getTrainingByIdShouldReturnResponse() {
//        Given
        when(trainingRepository.findById(idSavedTraining)).thenReturn(Optional.of(trainingWithDataSaved));
        when(trainingMapper.mapToResponse(trainingWithDataSaved)).thenReturn(responseWithData);


//        When
        TrainingResponse result = trainingService.getTrainingById(idSavedTraining);

//        Then
        assertThat(result).isSameAs(responseWithData);
    }

    @Test
    void getTrainingByIdShouldThrowRecordNotFoundException() {
//        Given
        when(trainingRepository.findById(missingId)).thenReturn(Optional.empty());

//        When Then
        assertThatThrownBy(() -> trainingService.getTrainingById(missingId))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessage("No training found: " + missingId);

    }
//
    @Test
    void deleteTrainingByIdShouldReturnTrue() {
//        Given
        when(trainingRepository.existsById(idSavedTraining)).thenReturn(true);

//        When
        boolean result = trainingService.deleteTrainingById(idSavedTraining);

//        Then
        assertThat(result).isTrue();
        verify(trainingRepository).existsById(idSavedTraining);
        verify(trainingRepository).deleteById(idSavedTraining);
    }

    @Test
    void deleteTrainingWithFakeIdShouldReturnFalse() {
//        Given
        when(trainingRepository.existsById(missingId)).thenReturn(false);

//        When
        boolean result = trainingService.deleteTrainingById(missingId);

//        Then
        assertThat(result).isFalse();
        verify(trainingRepository).existsById(missingId);
        verify(trainingRepository, never()).deleteById(missingId);
    }
}