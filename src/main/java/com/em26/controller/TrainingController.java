package com.em26.controller;

import com.em26.dto.TrainingRequest;
import com.em26.dto.TrainingResponse;
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

    @PostMapping
    public ResponseEntity<TrainingResponse> createTraining(@RequestBody TrainingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(trainingService.createTraining(request));
    }

    @PostMapping("/fake")
    public ResponseEntity<TrainingResponse> createFakeTraining(@RequestBody TrainingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(trainingService.createFakeTraining(request));
    }

    @GetMapping
    public ResponseEntity<List<TrainingResponse>> getAllTrainings() {
        return ResponseEntity.ok(trainingService.getAllTrainings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingResponse> getTrainingById(@PathVariable Long id) {
        return trainingService.getTrainingById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTrainingById(@PathVariable Long id) {
        boolean isDeleted = trainingService.deleteTrainingById(id);
        if(isDeleted) return ResponseEntity.ok("Training deleted");
        return ResponseEntity.notFound().build();
    }


}
