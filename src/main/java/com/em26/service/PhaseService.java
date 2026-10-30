package com.em26.service;

import com.em26.dto.IntervalRequest;
import com.em26.dto.PhaseRequest;
import com.em26.dto.PhaseResponse;
import com.em26.exception.RecordNotFoundException;
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

import java.util.ArrayList;
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

        Phase phase = phaseMapper.mapToEntity(training, request);

        assignPhaseTimeValues(phase, training.getPhaseList());

        phase.setHrAvg(calcHrAvg(training.getDataRowList(), phase));

        phaseRepository.save(phase);

        return phaseMapper.mapToResponse(phase);

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

//        validateStopTimeNotExceeding(phase);
//
        phaseList.add(phase);
//        return phase;

    }



    @Transactional
    public List<PhaseResponse> createInterval(Long trainingId, IntervalRequest request) {
//        int totalDuration = request.interval().stream()
//                .mapToInt(PhaseRequest::duration)
//                .sum();

        List<PhaseResponse> interval = new ArrayList<>();
        for (int i = 0; i < request.factor(); i++) {
            for (PhaseRequest phaseRequest : request.interval()) {
                interval.add(createPhase(trainingId, phaseRequest));
            }
        }
        return interval;
    }




//    todo: check if phase doesn't exceed training time with last datarow
}
