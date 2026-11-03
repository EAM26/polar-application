package com.em26.service;

import com.em26.dto.TrainingAnalysisResponse;
import com.em26.exception.RecordNotFoundException;
import com.em26.mapper.AnalysisMapper;
import com.em26.model.AnalysisBlock;
import com.em26.model.ElevationSummary;
import com.em26.model.Phase;
import com.em26.model.Training;
import com.em26.repository.TrainingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalysisBlockService {


    private final TrainingRepository trainingRepository;
    private final AnalyzerService analyzer;
    private final AnalysisMapper analysisMapper;

    @Transactional
    public TrainingAnalysisResponse createAnalysis(Long trainingId, int blockDuration) {
        Training training = trainingRepository.findById(trainingId).orElseThrow(() ->
                new RecordNotFoundException("No training found: " + trainingId));

//       todo: refactor to custom exception
        if (training.getPhaseList().isEmpty()) throw new RuntimeException("Minimum of 1 phase is required.");

        List<AnalysisBlock> blocks =  calculateBlocks(blockDuration, training.getPhaseList());

        return analysisMapper.mapToResponse(training, blocks);
    }

    private List<AnalysisBlock> calculateBlocks(int blockDuration, List<Phase> phaseList) {
        List<AnalysisBlock> blocks = new ArrayList<>();
        for (Phase phase : phaseList) {
            int amount = phase.getDuration() / blockDuration;
            for (int i = 0; i < amount; i++) {
                blocks.add(createSingleBlock(blockDuration, phase, blocks));
            }
            int remainingDuration = phase.getDuration() % blockDuration;
            if (remainingDuration > 0) {
                blocks.add(createSingleBlock(remainingDuration, phase, blocks));
            }
        }
        return blocks;
    }

    private AnalysisBlock createSingleBlock(int duration, Phase phase, List<AnalysisBlock> blocks) {
        AnalysisBlock block = new AnalysisBlock();
        block.setDuration(duration);
        block.setSequenceNumber(calculateSequenceNumber(blocks));
        block.setDescription(phase.getName());
        block.setPhaseId(phase.getId());
        if (blocks.isEmpty()) {
            block.setStartTime(0);
            block.setStopTime(duration - 1);
        } else {
            block.setStartTime(blocks.getLast().getStopTime() + 1);
            block.setStopTime(block.getStartTime() + duration - 1);
        }
        assignAnalysisValues(block, phase.getTraining());
        return block;
    }

    private int calculateSequenceNumber(List<AnalysisBlock> blocks) {
        if (blocks.isEmpty()) return 0;
        return blocks.getLast().getSequenceNumber() + 1;
    }

    private void assignAnalysisValues(AnalysisBlock block, Training training) {
        block.setHrAvg(analyzer.getAvgHeartRate(block.getStartTime(),
                block.getStopTime(), training));
        block.setCadenceAvg(analyzer.getAvgCadence(block.getStartTime(),
                block.getStopTime(), training));
        block.setPowerAvg(analyzer.getAvgPower(block.getStartTime(),
                block.getStopTime(), training));

        block.setDistance(analyzer.getDistancePerPeriod(block.getStartTime(), block.getStopTime(), training));

        block.setSpeedAvg(block.getDistance() / block.getDuration() * 3.6);
        block.setPaceAvg(60 / block.getSpeedAvg());

        block.setStartHr(analyzer.getHeartRateByTime(block.getStartTime(), training));
        block.setStopHr(analyzer.getHeartRateByTime(block.getStopTime(), training));
        block.setHrMax(analyzer.getMaximalHrForPeriod(block.getStartTime(), block.getStopTime(), training));
        block.setHrMin(analyzer.getMinimalHrForPeriod(block.getStartTime(), block.getStopTime(), training));

        ElevationSummary elevationSummary = analyzer.getElevation(block.getStartTime(), block.getStopTime(), training);
        block.setAscent(elevationSummary.getAscent());
        block.setDescent(elevationSummary.getDescent());
    }

}