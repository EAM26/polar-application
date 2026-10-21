package com.em26.service;

import com.em26.model.Training;
import com.em26.repository.TrainingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainingService {
    private final TrainingRepository trainingRepository;

    public Training createTraining(Training training) {
        return trainingRepository.save(training);
    }
}
