package com.em26.service;

import com.em26.dto.IntervalRequest;
import com.em26.dto.PhaseRequest;
import com.em26.dto.PhaseResponse;
import com.em26.exception.RecordNotFoundException;
import com.em26.mapper.PhaseMapper;
import com.em26.model.Phase;
import com.em26.model.Training;
import com.em26.repository.PhaseRepository;
import com.em26.repository.TrainingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PhaseService {

    private final PhaseRepository phaseRepository;
    private final TrainingRepository trainingRepository;
    private final PhaseMapper phaseMapper;


    @Transactional
    public PhaseResponse createPhase(Long trainingId, PhaseRequest request) {
        Training training = trainingRepository.findById(trainingId).orElseThrow(() ->
                new RecordNotFoundException("training not found: " + trainingId));

        Phase phase = phaseMapper.mapToEntity(training, request);

        Phase positionedPhase = positionPhaseInList(phase, training.getPhaseList());
        phaseRepository.save(positionedPhase);

        return phaseMapper.mapToResponse(phase);

    }

    private Phase positionPhaseInList(Phase phase, List<Phase> phaseList) {
        if (phaseList.isEmpty()) {
            phase.setStartTime(0);
            phase.setEndTime(phase.getDuration() - 1);

        } else {
            Phase lastPhase = phaseList.getLast();
            phase.setStartTime(lastPhase.getEndTime() + 1);
            phase.setEndTime(phase.getStartTime() + phase.getDuration() -1);
        }

        phaseList.add(phase);
        return phase;

    }

    @Transactional
    public List<PhaseResponse> createInterval(Long trainingId, IntervalRequest request) {
        List<PhaseResponse> interval = new ArrayList<>();
        for (int i = 0; i < request.factor(); i++) {
            for(PhaseRequest phaseRequest: request.interval()) {
                interval.add(createPhase(trainingId, phaseRequest));
            }
        }
        return interval;
    }

//    todo: create interval by looping through createPhase, (factor, {phases})


//    todo: check if phase doesn't exceed training time with last datarow
}
