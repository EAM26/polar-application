package com.em26.service;

import com.em26.mapper.DataRowMapper;
import com.em26.model.DataRow;
import com.em26.model.Training;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DataRowService {

    private final DataRowMapper mapper;

    public List<DataRow> createDataRows(List<String[]> csvRows, Training training) {
        List<String[]> values = csvRows.stream()
                .skip(3)
                .toList();

        return values.stream()
                .map(value -> mapper.mapToDataRow(value, training))
                .toList();
    }

//    private List<DataRow> getTrainingsDataFromCsv(List<String[]> csvRows) {
//        List<String[]> values = csvRows.stream()
//                .skip(3)
//                .toList();
//
//
//
//    }
}
