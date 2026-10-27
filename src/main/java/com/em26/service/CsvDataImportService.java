package com.em26.service;

import com.em26.parser.CustomCsvReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CsvDataImportService {

    private final CustomCsvReader csvReader;

    public void importCsv(String filePath) {
        List<String[]> csvRows = csvReader.readCsvRows(filePath);
        for (int i = 0; i < 10; i++) {
            System.out.println((csvRows.get(i)[2]));
        }
    }


}
