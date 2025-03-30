package com.hobbyFinder.hubby.controller;

import com.hobbyFinder.hubby.controller.routes.EvaluationRoutes;
import com.hobbyFinder.hubby.models.dto.evaluations.PostEvaluationDto;
import com.hobbyFinder.hubby.models.dto.evaluations.ResponseEvaluationDto;
import com.hobbyFinder.hubby.services.IServices.EvaluationInterface;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class EvaluationController{

    private EvaluationInterface evaluationInterface;

    @PostMapping(EvaluationRoutes.POST_EVALUATION_EVENT)
    public ResponseEntity<ResponseEvaluationDto> postAvaliationEvent(
            @PathVariable UUID idEvent,
            @RequestBody @Valid PostEvaluationDto postAvaliationDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.evaluationInterface.evaluateEvent(idEvent, postAvaliationDto, LocalDateTime.now()));
    }

    @GetMapping(EvaluationRoutes.GET_EVALUATION_EVENT)
    public ResponseEntity<Collection<ResponseEvaluationDto>> getAvaliationsFromEvent(
            @PathVariable UUID idEvent) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.evaluationInterface.getEventAvaliations(idEvent));
    }
}
