package com.hobbyFinder.hubby.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hobbyFinder.hubby.controller.routes.EventRoutes;
import com.hobbyFinder.hubby.models.dto.events.EventCreateDto;
import com.hobbyFinder.hubby.models.dto.events.EventDto;
import com.hobbyFinder.hubby.models.dto.events.EventPageDto;
import com.hobbyFinder.hubby.models.dto.events.EventPutDto;
import com.hobbyFinder.hubby.services.ServicesImpl.EventService;

import jakarta.validation.Valid;
import jdk.jshell.spi.ExecutionControl;

@RestController
@RequestMapping(EventRoutes.EVENT_BASE)
public class EventController {

    EventService eventService;

    public EventController(EventService eventService){
        this.eventService = eventService;
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
}
