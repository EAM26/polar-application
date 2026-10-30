package com.em26.controller;

import com.em26.dto.IntervalRequest;
import com.em26.dto.PhaseRequest;
import com.em26.dto.PhaseResponse;
import com.em26.service.PhaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PhaseController {

    private final PhaseService phaseService;

    @PostMapping("/trainings/{trainingId}/phases")
    public ResponseEntity<PhaseResponse> createPhase(@PathVariable Long trainingId, @RequestBody PhaseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(phaseService.createPhase(trainingId, request));
    }

    @PostMapping("/trainings/{trainingId}/interval")
    public ResponseEntity<List<PhaseResponse>> createInterval(@PathVariable Long trainingId,
                                                              @RequestBody IntervalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(phaseService.createInterval(trainingId, request));

    }
}
