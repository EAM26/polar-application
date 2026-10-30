package com.em26.service;

import com.em26.model.DataRow;
import com.em26.model.Training;
import com.em26.util.StatisticsCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyzerService {

    private final StatisticsCalculator calculator;

    public Double getAvgHeartRAte(int startTime, int stopTime, Training training) {
        List<Integer> hrColumn = training.getDataRowList().stream()
                .filter(dataRow -> dataRow.getIndexSeconds() >= startTime &&
                        dataRow.getIndexSeconds() <= stopTime)
                .map(DataRow::getHeartRate)
                .toList();
        if(hrColumn.contains(null)) return null;

        return calculator.getDoubleAvgFromIntList(hrColumn);
    }

    public Double getAvgCadence(int startTime, int stopTime, Training training) {
        List<Integer> cadenceColumn = training.getDataRowList().stream()
                .filter(dataRow -> dataRow.getIndexSeconds() >= startTime &&
                        dataRow.getIndexSeconds() <= stopTime)
                .map(DataRow::getCadence)
                .toList();
        if(cadenceColumn.contains(null)) return null;

        return calculator.getDoubleAvgFromIntList(cadenceColumn);
    }
}
