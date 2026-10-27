package com.em26.mapper;

import com.em26.model.DataRow;
import com.em26.model.Training;
import org.springframework.stereotype.Component;

@Component
public class DataRowMapper {


    public DataRow mapToDataRow(String[] value, Training training) {
        DataRow dataRow = new DataRow();
        dataRow.setTimeLabel(value[1]);
        dataRow.setTimeLabel(value[2]);
        dataRow.setTraining(training);
        return dataRow;

    }
}
