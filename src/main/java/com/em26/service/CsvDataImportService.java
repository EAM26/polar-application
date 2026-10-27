package com.em26.service;

import com.em26.parser.CustomCsvReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CsvDataImportService {

    private final CustomCsvReader csvReader;

    public List<String[]> importCsv(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            return csvReader.readCsvRows(inputStream);
        } catch (IOException e) {
            throw new UncheckedIOException(
                    "Error in reading file: ", e);
        }
    }

}
