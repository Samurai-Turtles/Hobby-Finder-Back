package com.hobbyFinder.hubby.controller;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

import com.hobbyFinder.hubby.models.dto.avaliations.PostAvaliationDto;
import com.hobbyFinder.hubby.models.dto.avaliations.ResponseAvaliationDto;
import com.hobbyFinder.hubby.models.dto.events.*;
import com.hobbyFinder.hubby.models.dto.participations.GetResponseParticipationEvent;
import com.hobbyFinder.hubby.models.dto.participations.UpdateParticipationDto;
import com.hobbyFinder.hubby.services.IServices.EventInterface;
import com.hobbyFinder.hubby.services.IServices.ParticipationInterface;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hobbyFinder.hubby.controller.routes.EventRoutes;

import jakarta.validation.Valid;
import jdk.jshell.spi.ExecutionControl;

@RestController
public class EventController {

    @Autowired
    private ParticipationInterface participationInterface;

    @Autowired
    private EventInterface eventService;

    @PostMapping(EventRoutes.POST_EVENT)
    public ResponseEntity<Void> post(
            @RequestBody @Valid EventCreateDto eventCreateDto) {
        eventService.registerEvent(eventCreateDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
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
    public ResponseEntity<Page<GetResponseParticipationEvent>> getAllParticipations
            (@PathVariable UUID id, @RequestParam(defaultValue = "20") int size, @RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(participationInterface.getParticipationEvents(id, pageable));

    }

    @DeleteMapping(EventRoutes.EXPEL_USER_FROM_EVENT)
    public ResponseEntity<Void> deleteUserParticipationFromEvent(@PathVariable UUID idEvent, @PathVariable UUID idParticipation) {
        participationInterface.deleteUserFromEvent(idEvent, idParticipation);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PostMapping(EventRoutes.POST_AVALIATION_EVENT)
    public ResponseEntity<ResponseAvaliationDto> postAvaliationEvent(
            @PathVariable UUID idEvent,
            @RequestBody PostAvaliationDto postAvaliationDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.participationInterface.evaluateEvent(idEvent, postAvaliationDto, LocalDateTime.now()));
    }

    @GetMapping(EventRoutes.GET_AVALIATION_EVENT)
    public ResponseEntity<Collection<ResponseAvaliationDto>> getAvaliationsFromEvent(
            @PathVariable UUID idEvent) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.participationInterface.getEventAvaliations(idEvent));
    }

    @PutMapping(EventRoutes.PARTICIPATION_MANAGEMENT)
    public ResponseEntity<UpdateParticipationDto> updateParticipationManagement(
            @PathVariable UUID idEvent,
            @PathVariable UUID idParticipation,
            @RequestBody UpdateParticipationDto updateParticipationDto) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.participationInterface.participationManagement(idEvent, idParticipation, updateParticipationDto));

    }
}
