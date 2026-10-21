package com.em26.service;

import com.em26.dto.TrainingRequest;
import com.em26.dto.TrainingResponse;
import com.em26.mapper.TrainingMapper;
import com.em26.model.Training;
import com.em26.repository.TrainingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainingService {
    private final TrainingRepository trainingRepository;
    private final TrainingMapper trainingMapper;



    public TrainingResponse createTraining(TrainingRequest request) {
        Training savedTraining =  trainingRepository.save(trainingMapper.mapToEntity(request));
        return trainingMapper.mapToResponse(savedTraining);

    }
}
