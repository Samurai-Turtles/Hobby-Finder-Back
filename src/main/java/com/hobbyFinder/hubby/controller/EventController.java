package com.hobbyFinder.hubby.controller;

import java.util.UUID;

import com.hobbyFinder.hubby.models.dto.events.*;
import com.hobbyFinder.hubby.services.IServices.EventInterface;
import com.hobbyFinder.hubby.services.IServices.ParticipationInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hobbyFinder.hubby.controller.routes.EventRoutes;

import jakarta.validation.Valid;

@RestController
public class EventController {

    @Autowired
    private EventInterface eventService;

    @PostMapping(EventRoutes.POST_EVENT)
    public ResponseEntity<EventDto> post(
            @RequestBody @Valid EventCreateDto eventCreateDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventService.registerEvent(eventCreateDto));
    }

    @GetMapping(EventRoutes.GET_EVENT_BY_ID)
    public ResponseEntity<EventDto> get
            (@PathVariable UUID id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.getEvent(id));
    }

    @PutMapping(EventRoutes.PUT_EVENT)
    public ResponseEntity<EventDto> put(
            @PathVariable UUID id,
            @RequestBody EventPutDto eventPutDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.updateEvent(id,eventPutDto));
    }

    @DeleteMapping(EventRoutes.DELETE_EVENT)
    public ResponseEntity<Void> delete(
            @PathVariable UUID id) {
        eventService.deleteEvent(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}
