package com.em26.util;

import org.springframework.stereotype.Component;

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
}
