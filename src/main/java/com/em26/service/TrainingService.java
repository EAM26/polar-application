package com.em26.service;

import com.em26.dto.TrainingResponse;
import com.em26.mapper.TrainingMapper;
import com.em26.model.Training;
import com.em26.repository.TrainingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainingService {
    private final TrainingRepository trainingRepository;
    private final TrainingMapper trainingMapper;


    public TrainingResponse createTraining() {
        Training training = trainingMapper.mapToEntity();
        return trainingMapper.mapToResponse(trainingRepository.save(training));
    }


    public List<TrainingResponse> getAllTrainings() {
        return trainingRepository.findAll()
                .stream()
                .map(trainingMapper::mapToResponse)
                .toList();
    }

    public Optional<TrainingResponse> getTrainingById(Long id) {
        return trainingRepository.findById(id)
                .map(trainingMapper::mapToResponse);
    }

    public boolean deleteTrainingById(Long id) {
        if(trainingRepository.existsById(id)) {
            trainingRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public TrainingResponse createFakeTraining() {
        Training training = trainingMapper.mapToEntity();
        training.setName("RUNNING: 2026-10-21 08:05:10");
        training.setSportType("RUNNING");
        training.setStartTime(LocalTime.of(8, 10, 20));
        training.setStartDate(LocalDate.of(2026, 10, 1));
        training.setDurationSeconds(3600);
        training.setDistanceMeters(10000);

         return trainingMapper.mapToResponse(trainingRepository.save(training));
    }
}
