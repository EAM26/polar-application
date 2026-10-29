package com.em26.service;

import com.em26.dto.PhaseRequest;
import com.em26.dto.PhaseResponse;
import com.em26.exception.RecordNotFoundException;
import com.em26.mapper.PhaseMapper;
import com.em26.model.Phase;
import com.em26.model.Training;
import com.em26.repository.PhaseRepository;
import com.em26.repository.TrainingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PhaseService {

    private final PhaseRepository phaseRepository;
    private final TrainingRepository trainingRepository;
    private final PhaseMapper phaseMapper;


    public PhaseResponse createPhase(Long trainingId, PhaseRequest request) {
        Training training = trainingRepository.findById(trainingId).orElseThrow(() ->
                new RecordNotFoundException("training not found: " + trainingId));

        Phase phase = phaseMapper.mapToEntity(training, request);
        phaseRepository.save(phase);

        return phaseMapper.mapToResponse(phase);

    }
}
