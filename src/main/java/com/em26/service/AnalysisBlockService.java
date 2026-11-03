package com.em26.service;

import com.em26.exception.RecordNotFoundException;
import com.em26.model.AnalysisBlock;
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

    @Transactional
    public List<AnalysisBlock> createAnalysis(Long trainingId, int blockDuration) {
        Training training = trainingRepository.findById(trainingId).orElseThrow(() ->
                new RecordNotFoundException("No training found: " + trainingId));

//       todo: refactor to custom exception
        if (training.getPhaseList().isEmpty()) throw new RuntimeException("Minimum of 1 phase is required.");

        List<AnalysisBlock> blocks = calculateBlocks(blockDuration, training.getPhaseList());
        return blocks;
    }

    private List<AnalysisBlock> calculateBlocks(int blockDuration, List<Phase> phaseList) {
        List<AnalysisBlock> blocks = new ArrayList<>();
        int sequenceNumber = 0;
        for (Phase phase : phaseList) {
            int amount = phase.getDuration() / blockDuration;
            for (int i = 0; i < amount; i++) {
                blocks.add(createSingleBlock(blockDuration, phase, sequenceNumber, blocks));
                sequenceNumber++;
            }
            int remainingDuration = phase.getDuration() % blockDuration;
            if (remainingDuration > 0) {
                blocks.add(createSingleBlock(remainingDuration, phase, sequenceNumber, blocks));
                sequenceNumber++;
            }
        }
        showAnalysisBlocks(blocks);
        return blocks;
    }

    private AnalysisBlock createSingleBlock(int duration, Phase phase, int sequenceNumber, List<AnalysisBlock> blocks) {
        AnalysisBlock block = new AnalysisBlock();
        block.setActualDuration(duration);
        block.setSequenceNumber(sequenceNumber);
        block.setPhaseId(phase.getId());
        if (blocks.isEmpty()) {
            block.setStartTime(0);
            block.setStopTime(duration - 1);
        } else {
            block.setStartTime(blocks.getLast().getStartTime() + 1);

            block.setStopTime(blocks.getLast().getStopTime() + duration);
        }
        return block;


    }

    private void showAnalysisBlocks(List<AnalysisBlock> blocks) {
        for (AnalysisBlock block : blocks) {
            System.out.println(block.getSequenceNumber() + ": " + block.getActualDuration());
        }
    }
}

//    private List<AnalysisBlock> createBlocksPerPeriod(Training training, int block) {
//    }
//}
