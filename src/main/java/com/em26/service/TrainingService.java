package com.em26.service;

import com.em26.dto.TrainingResponse;
import com.em26.exception.RecordNotFoundException;
import com.em26.mapper.TrainingMapper;
import com.em26.model.Training;
import com.em26.repository.TrainingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingService {
    private final TrainingRepository trainingRepository;
    private final TrainingMapper trainingMapper;
    private final CsvDataImportService importService;


    public TrainingResponse createTraining(MultipartFile file) {
        List<String[]> csvRows = importService.importCsv(file);
        Training training = trainingMapper.mapToEntity();
        return trainingMapper.mapToResponse(trainingRepository.save(training));
    }


    public List<TrainingResponse> getAllTrainings() {
        return trainingRepository.findAll()
                .stream()
                .map(trainingMapper::mapToResponse)
                .toList();
    }

    public TrainingResponse getTrainingById(Long id) {
        Training training = trainingRepository.findById(id).orElseThrow(() ->
                new RecordNotFoundException("No training found: " + id));
        return trainingMapper.mapToResponse(training);
    }

    public boolean deleteTrainingById(Long id) {
        if (trainingRepository.existsById(id)) {
            trainingRepository.deleteById(id);
            return true;
        }
        return false;
    }

//    public TrainingResponse createFakeTraining() {
//        Training training = trainingMapper.mapToEntity();
//        training.setName("RUNNING: 2026-10-21 08:05:10");
//        training.setSportType("RUNNING");
//        training.setStartTime(LocalTime.of(8, 10, 20));
//        training.setStartDate(LocalDate.of(2026, 10, 1));
//        training.setDurationSeconds(3600);
//        training.setDistanceMeters(10000);
//
//         return trainingMapper.mapToResponse(trainingRepository.save(training));
//    }
}
