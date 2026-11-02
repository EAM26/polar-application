package com.em26.util;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class StatisticsCalculator {

    public Double getDoubleAvgFromIntList(List<Integer> column) {
        if(column.isEmpty() || column.contains(null)) return null;
        Integer sum = 0;
        for(Integer value: column) {
            sum += value;
        }
        return (double) sum/column.size();
    }

    public Integer getMaxValueFromList(List<Integer> column) {
        return Collections.max(column);
    }

    public Integer getMinValueFromList(List<Integer> column) {
        return Collections.min(column);
    }


}
