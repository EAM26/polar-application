package com.em26.parser;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class CustomCsvReader {

    public List<String[]> readCsvRows(InputStream inputStream) {
        try (Reader reader =
                     new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             CSVReader csvReader = new CSVReaderBuilder(reader).build()) {

            return csvReader.readAll();

        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV file", e);
        } catch (CsvException e) {
            throw new RuntimeException("Failed to process CSV content", e);
        }
    }
}
