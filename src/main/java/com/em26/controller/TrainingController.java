package com.em26.controller;

import com.em26.dto.TrainingResponse;
import com.em26.service.CsvDataImportService;
import com.em26.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;
    private final CsvDataImportService importService;

    @PostMapping
    public ResponseEntity<TrainingResponse> createTraining() {
        return ResponseEntity.status(HttpStatus.CREATED).body(trainingService.createTraining());
    }

//    @PostMapping("/fake")
//    public ResponseEntity<TrainingResponse> createFakeTraining() {
//        return ResponseEntity.status(HttpStatus.CREATED).body(trainingService.createFakeTraining());
//    }

    @GetMapping
    public ResponseEntity<List<TrainingResponse>> getAllTrainings() {
        return ResponseEntity.ok(trainingService.getAllTrainings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingResponse> getTrainingById(@PathVariable Long id) {
        return ResponseEntity.ok(trainingService.getTrainingById(id));
    }

    @GetMapping("/read-csv")
    public String runCsvReader(@RequestBody String filePath) {
        importService.importCsv(filePath);
        return "Method finished";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTrainingById(@PathVariable Long id) {
        boolean isDeleted = trainingService.deleteTrainingById(id);
        if(isDeleted) return ResponseEntity.ok("Training deleted");
        return ResponseEntity.notFound().build();
    }


}
