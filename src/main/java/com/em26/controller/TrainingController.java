package com.em26.controller;

import com.em26.dto.TrainingRequest;
import com.em26.dto.TrainingResponse;
import com.em26.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/training")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;

    @PostMapping
    public ResponseEntity<TrainingResponse> createTraining(@RequestBody TrainingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body( trainingService.createTraining(request));
    }



}
