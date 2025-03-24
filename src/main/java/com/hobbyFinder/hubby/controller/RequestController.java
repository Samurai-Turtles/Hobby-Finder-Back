package com.hobbyFinder.hubby.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hobbyFinder.hubby.controller.routes.RequestRoutes;
import com.hobbyFinder.hubby.models.dto.participationRequest.ParticipationRequestEventDto;
import com.hobbyFinder.hubby.models.dto.participationRequest.ParticipationRequestUserDto;
import com.hobbyFinder.hubby.services.IServices.RequestInterface;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class RequestController {

    private RequestInterface requestService;

    @PostMapping(RequestRoutes.POST_REQUEST)
    public ResponseEntity<Void> createNewParticipationRequest(@PathVariable UUID targetEventId) {

        requestService.newRequest(targetEventId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(RequestRoutes.GET_REQUESTS_BY_EVENT)
    public ResponseEntity<Page<ParticipationRequestEventDto>> getParticipationRequestsByEvent(
            @PathVariable UUID targetEventId,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ParticipationRequestEventDto> requestPages = requestService.getAllEventRequests(targetEventId, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(requestPages);
    }

    @GetMapping(RequestRoutes.GET_REQUESTS_BY_USER)
    public ResponseEntity<Page<ParticipationRequestUserDto>> getParticipationRequestsByUser(
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ParticipationRequestUserDto> requestPage = requestService.getAllUserRequests(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(requestPage);
    }

    @PutMapping(RequestRoutes.ACCEPT_REQUEST)
    public ResponseEntity<Void> acceptParticipationRequest(
            @PathVariable UUID targetEventId,
            @PathVariable UUID targetRequestId) {

        requestService.acceptRequest(targetEventId, targetRequestId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(RequestRoutes.DELETE_REQUEST)
    public ResponseEntity<Void> declineParticipationRequest(
            @PathVariable UUID targetEventId,
            @PathVariable UUID targetRequestId) {

        requestService.declineRequest(targetEventId, targetRequestId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(RequestRoutes.DELETE_REQUEST_BY_USER)
    public ResponseEntity<Void> deleteParticipationRequest(@PathVariable UUID targetRequestId) {

        requestService.deleteRequestByUser(targetRequestId);
        return ResponseEntity.noContent().build();
    }

}
