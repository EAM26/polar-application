package com.em26.service;

import com.em26.model.DataRow;
import com.em26.model.ElevationSummary;
import com.em26.model.Training;
import com.em26.util.StatisticsCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnalyzerService {

    private final StatisticsCalculator calculator;
    private final DataRowService dataRowService;

    public Double getAvgHeartRate(int startTime, int stopTime, Training training) {
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

    public Double getAvgPower(int startTime, int stopTime, Training training) {
        List<Integer> cadenceColumn = training.getDataRowList().stream()
                .filter(dataRow -> dataRow.getIndexSeconds() >= startTime &&
                        dataRow.getIndexSeconds() <= stopTime)
                .map(DataRow::getPower)
                .toList();
        if(cadenceColumn.contains(null)) return null;

        return calculator.getDoubleAvgFromIntList(cadenceColumn);
    }

    public Double getDistancePerPeriod(int startTime, int stopTime,  Training training) {
        Optional<DataRow> start = dataRowService.getDataRowByTime(startTime, training);
        Optional<DataRow> stop = dataRowService.getDataRowByTime(stopTime, training);

        if(stop.isPresent() && start.isPresent() &&
                start.get().getDistance() != null && stop.get().getDistance() != null)
            return stop.get().getDistance() - start.get().getDistance();
        return null;
    }

    public Integer getHeartRateByTime(int time, Training training) {
        Optional<DataRow> dataRowOptional = dataRowService.getDataRowByTime(time, training);
        return dataRowOptional.map(DataRow::getHeartRate).orElse(null);
    }

    public Integer getMaximalHrForPeriod(int startTime, int stopTime, Training training) {
        List<Integer> hrColumn = training.getDataRowList().stream()
                .filter(dataRow -> dataRow.getIndexSeconds() >= startTime && dataRow.getIndexSeconds() <= stopTime)
                .map(DataRow::getHeartRate)
                .toList();
        return calculator.getMaxValueFromList(hrColumn);
    }

    public Integer getMinimalHrForPeriod(int startTime, int stopTime, Training training) {
        List<Integer> hrColumn = training.getDataRowList().stream()
                .filter(dataRow -> dataRow.getIndexSeconds() >= startTime && dataRow.getIndexSeconds() <= stopTime)
                .map(DataRow::getHeartRate)
                .toList();
        return calculator.getMinValueFromList(hrColumn);
    }

    public ElevationSummary getElevation(int startTime, int stopTime, Training training) {
        List<Integer> altitudeColumn = training.getDataRowList().stream()
                .filter(dataRow -> dataRow.getIndexSeconds() >= startTime && dataRow.getIndexSeconds() <= stopTime)
                .map(DataRow::getAltitude)
                .toList();
        return calculator.calculateTotalIncreaseAndDecrease(altitudeColumn);

    }


}
