package com.em26.service;

import com.em26.dto.IntervalRequest;
import com.em26.dto.PhaseRequest;
import com.em26.dto.PhaseResponse;
import com.em26.exception.RecordNotFoundException;
import com.em26.exception.TrainingDurationExceededException;
import com.em26.mapper.PhaseMapper;
import com.em26.model.DataRow;
import com.em26.model.Phase;
import com.em26.model.Training;
import com.em26.repository.PhaseRepository;
import com.em26.repository.TrainingRepository;
import com.em26.util.StatisticsCalculator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PhaseService {

    private final PhaseRepository phaseRepository;
    private final TrainingRepository trainingRepository;
    private final PhaseMapper phaseMapper;
    private final StatisticsCalculator calculator;


    @Transactional
    public PhaseResponse createPhase(Long trainingId, PhaseRequest request) {
        Training training = trainingRepository.findById(trainingId).orElseThrow(() ->
                new RecordNotFoundException("training not found: " + trainingId));

        validateRemainingTime(training, request.duration());

        Phase phase = addSinglePhaseToList(training, request);
        return phaseMapper.mapToResponse(phase);

    }

    private Phase addSinglePhaseToList(Training training, PhaseRequest request) {
        Phase phase = phaseMapper.mapToEntity(training, request);
        assignPhaseTimeValues(phase, training.getPhaseList());

        phase.setHrAvg(calcHrAvg(training.getDataRowList(), phase));
        training.addPhase(phase);
        return phaseRepository.save(phase);

    }

    private Double calcHrAvg(List<DataRow> dataRowList, Phase phase) {
        List<Integer> hrColumn = dataRowList.stream()
                .filter(dataRow -> dataRow.getIndexSeconds() >= phase.getStartTime() &&
                        dataRow.getIndexSeconds() <= phase.getStopTime())
                .map(DataRow::getHeartRate)
                .toList();
        return calculator.getDoubleAvgFromIntList(hrColumn);
    }

    private void assignPhaseTimeValues(Phase phase, List<Phase> phaseList) {
        if (phaseList.isEmpty()) {
            phase.setStartTime(0);
            phase.setStopTime(phase.getDuration() - 1);

        } else {
            Phase lastPhase = phaseList.getLast();
            phase.setStartTime(lastPhase.getStopTime() + 1);
            phase.setStopTime(phase.getStartTime() + phase.getDuration() - 1);
        }


    }

    public void validateRemainingTime(Training training, int duration) {
        int currentStopTime;
        if (training.getPhaseList() == null || training.getPhaseList().isEmpty()) currentStopTime = -1;
        else currentStopTime = training.getPhaseList().getLast().getStopTime();

        int projectedRemainingTime = training.getDataRowList().getLast().getIndexSeconds() - currentStopTime - duration;

        if (projectedRemainingTime < 0) {
            throw new TrainingDurationExceededException(
                    "The requested duration exceeds the remaining training time by "
                            + Math.abs(projectedRemainingTime) + " seconds."
            );
        }
    }

    @Transactional
    public List<PhaseResponse> createInterval(Long trainingId, IntervalRequest request) {
        Training training = trainingRepository.findById(trainingId).orElseThrow(() ->
                new RecordNotFoundException("training not found: " + trainingId));

        int totalDurationInterval = request.factor() * request.interval().stream()
                .mapToInt(PhaseRequest::duration)
                .sum();

        validateRemainingTime(training, totalDurationInterval);

        for (int i = 0; i < request.factor(); i++) {
            request.interval().forEach(phaseRequest -> addSinglePhaseToList(training, phaseRequest));
        }

        return training.getPhaseList().stream()
                .map(phaseMapper::mapToResponse)
                .toList();
    }


//    todo: check if phase doesn't exceed training time with last datarow
}
