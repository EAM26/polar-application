package com.em26.util;

import com.em26.model.ElevationSummary;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class StatisticsCalculator {

    public Double getDoubleAvgFromIntList(List<Integer> column) {
        if (column.isEmpty() || column.contains(null)) return null;
        Integer sum = 0;
        for (Integer value : column) {
            sum += value;
        }
        return (double) sum / column.size();
    }

    public Integer getMaxValueFromList(List<Integer> column) {
        return Collections.max(column);
    }

    public Integer getMinValueFromList(List<Integer> column) {
        return Collections.min(column);
    }


    public ElevationSummary calculateTotalIncreaseAndDecrease(List<Integer> altitudeColumn) {
        ElevationSummary elevation = new ElevationSummary();
        if (altitudeColumn.contains(null)) {
            elevation.setAscent(null);
            elevation.setDescent(null);
        }

        int increase = 0;
        int decrease = 0;

        for (int i = 1; i < altitudeColumn.size(); i++) {
            if (altitudeColumn.get(i) > altitudeColumn.get(i - 1)) {
                increase += altitudeColumn.get(i) - altitudeColumn.get(i - 1);
            }
            if (altitudeColumn.get(i) < altitudeColumn.get(i - 1)) {
                decrease += altitudeColumn.get(i-1) - altitudeColumn.get(i);
            }

        }
        elevation.setAscent(increase);
        elevation.setDescent(decrease);

        return elevation;

    }
}
