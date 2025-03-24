package com.hobbyFinder.hubby.controller;

import java.util.Optional;
import java.util.UUID;

import com.hobbyFinder.hubby.models.dto.events.*;
import com.hobbyFinder.hubby.controller.Constants.PageConstants;
import com.hobbyFinder.hubby.services.IServices.EventInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(EventRoutes.GET_EVENT_BY_AUTH_USER)
    public ResponseEntity<Page<EventDto>> GetByAuthUser
            (@RequestParam Optional<Double>     latitude,
             @RequestParam Optional<Double>     longitude,
             @RequestParam Optional<Integer>    eventPerPage,
             @RequestParam Optional<Integer>    page,
             @RequestParam Optional<String>     name) {

        Pageable pageable = PageRequest.of(page.orElse(PageConstants.PAGE_INDEX), eventPerPage.orElse(PageConstants.EVENTS_PER_PAGE));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.getEventByAuthUser(latitude, longitude, name, pageable));
    }

    @GetMapping(EventRoutes.GET_EVENT_BY_USER_ID)
    public ResponseEntity<Page<EventDto>> GetByUserId
            (@PathVariable UUID userId,
             @RequestParam Optional<Integer>    eventPerPage,
             @RequestParam Optional<Integer>    page,
             @RequestParam Optional<String>     name) {

        Pageable pageable = PageRequest.of(page.orElse(PageConstants.PAGE_INDEX), eventPerPage.orElse(PageConstants.EVENTS_PER_PAGE));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.getByUserId(userId, name, pageable));
    }

}
