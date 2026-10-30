package com.em26.controller;

import com.em26.dto.TrainingResponse;
import com.em26.service.DataRowService;
import com.em26.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/training")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;
    private final DataRowService dataRowService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TrainingResponse> createTraining(MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(trainingService.createTraining(file));
    }


    @GetMapping
    public ResponseEntity<List<TrainingResponse>> getAllTrainings() {
        return ResponseEntity.ok(trainingService.getAllTrainings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingResponse> getTrainingById(@PathVariable Long id) {
        return ResponseEntity.ok(trainingService.getTrainingById(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTrainingById(@PathVariable Long id) {
        boolean isDeleted = trainingService.deleteTrainingById(id);
        if (isDeleted) return ResponseEntity.ok("Training deleted");
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{trainingId}/data-row")
    public ResponseEntity<String> deleteDataRowsOfTraining(@PathVariable Long trainingId) {
        System.out.println("controller running");
        boolean isDeleted = dataRowService.deleteAllDataRowsOfTraining(trainingId);
        if(isDeleted) return ResponseEntity.ok("Data rows deleted.");
        return ResponseEntity.badRequest().body("No data rows found");

    }

}
