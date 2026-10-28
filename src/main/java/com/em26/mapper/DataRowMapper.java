package com.em26.mapper;

import com.em26.model.DataRow;
import com.em26.model.Training;
import org.springframework.stereotype.Component;

@Component
public class DataRowMapper {


    public DataRow mapToDataRow(String[] value, Training training, int indexSeconds) {
        DataRow dataRow = new DataRow();
        dataRow.setIndexSeconds(indexSeconds);
        dataRow.setTimeLabel(value[1]);
        dataRow.setHeartRate(parseStringToInteger(value[2]));
        dataRow.setCadence(parseStringToInteger(value[5]));
        dataRow.setAltitude(parseStringToInteger(value[6]));
        dataRow.setStride(parseStringToInteger(value[7]));
        dataRow.setDistance(parseStringToDouble(value[8]));
        dataRow.setTemperature(parseStringToDouble(value[9]));
        dataRow.setPower(parseStringToInteger(value[10]));
        dataRow.setTraining(training);
        return dataRow;
    }

    private Integer parseStringToInteger(String stringValue) {
        if(stringValue == null || stringValue.isBlank()) {
            return null;
        }
        return Integer.parseInt(stringValue);
    }

    private Double parseStringToDouble(String stringValue) {
        if(stringValue == null || stringValue.isBlank()) {
            return null;
        }
        return Double.parseDouble(stringValue);
    }
}
