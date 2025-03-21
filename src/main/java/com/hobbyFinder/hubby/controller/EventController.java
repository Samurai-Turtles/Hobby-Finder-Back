package com.hobbyFinder.hubby.controller;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.hobbyFinder.hubby.models.dto.avaliations.PostAvaliationDto;
import com.hobbyFinder.hubby.models.dto.avaliations.ResponseAvaliationDto;
import com.hobbyFinder.hubby.models.dto.events.*;
import com.hobbyFinder.hubby.models.dto.participations.GetResponseParticipationEvent;
import com.hobbyFinder.hubby.services.ServicesImpl.ParticipationServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hobbyFinder.hubby.controller.routes.EventRoutes;
import com.hobbyFinder.hubby.services.ServicesImpl.EventService;

import jakarta.validation.Valid;
import jdk.jshell.spi.ExecutionControl;

@RestController
public class EventController {

    private final ParticipationServiceImpl participationServiceImpl;
    EventService eventService;

    public EventController(EventService eventService, ParticipationServiceImpl participationServiceImpl){
        this.eventService = eventService;
        this.participationServiceImpl = participationServiceImpl;
    }

    @PostMapping(EventRoutes.POST_EVENT)
    public ResponseEntity<EventDto> post(
            @RequestBody @Valid EventCreateDto eventCreateDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventService.registerEvent(eventCreateDto));
    }

    @GetMapping(EventRoutes.GET_EVENT_BY_AUTH_USER)
    public ResponseEntity<EventPageDto> getByAuthUser(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam int eventsByPage,
            @RequestParam int pageNumber,
            @RequestParam String Name) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Não implementado!");
    }

    @GetMapping(EventRoutes.GET_EVENT_BY_USER_ID)
    public ResponseEntity<EventPageDto> getByUserId(
            @PathVariable UUID id,
            @RequestParam double EventsByPage,
            @RequestParam int page,
            @RequestParam String Name) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Não implementado!");
    }

    @GetMapping(EventRoutes.GET_EVENT_BY_ID)
    public ResponseEntity<EventDto> get(@PathVariable UUID id) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Não implementado!");
    }

    @PutMapping(EventRoutes.PUT_EVENT)
    public ResponseEntity<EventDto> put(
            @PathVariable UUID id,
            @RequestBody EventPutDto eventPutDto) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Não implementado!");
    }

    @DeleteMapping(EventRoutes.DELETE_EVENT)
    public ResponseEntity<Void> delete(
            @PathVariable UUID id) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Não implementado!");
    }

    @GetMapping(EventRoutes.GET_ALL_EVENT_PARTICIPATIONS)
    public ResponseEntity<List<GetResponseParticipationEvent>> getAllParticipations(@PathVariable UUID id) {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(eventService.getParticipationsEvent(id));
    }

    @DeleteMapping(EventRoutes.EXPEL_USER_FROM_EVENT)
    public ResponseEntity<Void> deleteUserParticipationFromEvent(@PathVariable UUID idEvent, @PathVariable UUID idParticipation) {
        participationServiceImpl.deleteUserFromEvent(idEvent, idParticipation);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PostMapping(EventRoutes.POST_AVALIATION_EVENT)
    public ResponseEntity<ResponseAvaliationDto> postAvaliationEvent(
            @PathVariable UUID idEvent,
            @RequestBody PostAvaliationDto postAvaliationDto) {
        return ResponseEntity
                .ok()
                .body(eventService.evaluateEvent(idEvent, postAvaliationDto, LocalDateTime.now()));
    }

    @GetMapping(EventRoutes.GET_AVALIATION_EVENT)
    public ResponseEntity<Collection<ResponseAvaliationDto>> getAvaliationsFromEvent(
            @PathVariable UUID idEvent) {
        return ResponseEntity
                .ok()
                .body(eventService.getAvaliationsEvent(idEvent));
    }
}
