package com.em26.service;

import com.em26.dto.TrainingResponse;
import com.em26.exception.RecordNotFoundException;
import com.em26.mapper.TrainingMapper;
import com.em26.model.Training;
import com.em26.repository.TrainingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TrainingService {
    private final TrainingRepository trainingRepository;
    private final TrainingMapper trainingMapper;
    private final CsvDataImportService importService;
    private final DataRowService dataRowService;


    @Transactional
    public TrainingResponse createTraining(MultipartFile file) {

        //        imports all the csv rows to one object
        List<String[]> csvRows = importService.importCsv(file);

        //        creates a map for of first two rows (headers and values)
        Map<String, String> metaData = getMetaDataFromCsvRows(csvRows);

        //        creates a training and sets meta fields
        Training training = trainingMapper.mapMetaDataToTraining(metaData);

//        reads row 2 till end  in one List of String[]
//        maps the List to List<DataRow>
//        sets TrainingId in Data row and save DataRow (flush & Transactional)
        training.setDataRowList(dataRowService.createDataRows(csvRows, training));

//        save and return training
        return trainingMapper.mapToResponse(trainingRepository.save(training));
    }

    private Map<String, String> getMetaDataFromCsvRows(List<String[]> csvRows) {
        String[] headers = csvRows.get(0);
        String[] values = csvRows.get(1);

        if(headers.length != values.length) {
            throw new IllegalArgumentException("Error in reading metadata: mismatch length for headers and values");
        }

        Map<String, String> metaData = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            metaData.put(headers[i], values[i]);
        }
        return metaData;



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
