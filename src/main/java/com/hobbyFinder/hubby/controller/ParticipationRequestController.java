package com.hobbyFinder.hubby.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hobbyFinder.hubby.controller.routes.ParticipationRequestRoutes;
import com.hobbyFinder.hubby.models.dto.participationRequest.ParticipationRequestGetDTO;
import com.hobbyFinder.hubby.models.dto.participationRequest.ParticipationRequestResponseDTO;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
public class ParticipationRequestController {

    @PostMapping(ParticipationRequestRoutes.POST_REQUEST)
    public ResponseEntity<Void> createNewParticipationRequest(@PathVariable UUID targetEventId) {
        return null;
    }

    @GetMapping(ParticipationRequestRoutes.GET_REQUESTS_BY_EVENT)
    public ResponseEntity<ParticipationRequestResponseDTO> getParticipationRequestsByEvent(
            @PathVariable UUID targetEventId, @RequestBody ParticipationRequestGetDTO participationRequestDto) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(null);
    }

    @GetMapping(ParticipationRequestRoutes.GET_REQUESTS_BY_USER)
    public ResponseEntity<ParticipationRequestResponseDTO> getParticipationRequestsByUser(
            @RequestBody ParticipationRequestGetDTO participationRequestGetDTO) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(null);
    }

    @PutMapping(ParticipationRequestRoutes.ACCEPT_REQUEST)
    public ResponseEntity<Void> acceptParticipationRequest(@PathVariable UUID targetEventId,
            @PathVariable UUID targetRequestId) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(null);
    }

    @DeleteMapping(ParticipationRequestRoutes.DELETE_REQUEST)
    public ResponseEntity<Void> declineParticipationRequest(@PathVariable UUID targetEventId,
            @PathVariable UUID targetRequestId) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(null);
    }

    @DeleteMapping(ParticipationRequestRoutes.DELETE_REQUEST_BY_USER)
    public ResponseEntity<Void> deleteParticipationRequest(@PathVariable UUID targetRequestId) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(null);
    }

}
