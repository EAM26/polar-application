package com.em26.service;

import com.em26.exception.RecordNotFoundException;
import com.em26.mapper.DataRowMapper;
import com.em26.model.DataRow;
import com.em26.model.Training;
import com.em26.repository.TrainingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class DataRowService {

    private final DataRowMapper mapper;
    private final TrainingRepository trainingRepository;

    public List<DataRow> createDataRows(List<String[]> csvRows, Training training) {
        AtomicInteger indexSeconds = new AtomicInteger();
        List<String[]> values = csvRows.stream()
                .skip(3)
                .toList();

        return values.stream()
                .map(value -> mapper.mapToDataRow(value, training, indexSeconds.getAndIncrement()))
                .toList();
    }

    @Transactional
    public boolean deleteAllDataRowsOfTraining(Long trainingId) {
        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() ->
                        new RecordNotFoundException(
                                "Training not found: " + trainingId));

        if (training.getDataRowList().isEmpty()) {
            return false;
        }

        training.getDataRowList().forEach(dataRow -> dataRow.setTraining(null));

        training.getDataRowList().clear();
        return true;
    }



}
