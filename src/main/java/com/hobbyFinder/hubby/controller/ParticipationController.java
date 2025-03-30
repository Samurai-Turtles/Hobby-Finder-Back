package com.hobbyFinder.hubby.controller;

import com.hobbyFinder.hubby.controller.routes.ParticipationRoutes;
import com.hobbyFinder.hubby.controller.routes.UserRoutes;
import com.hobbyFinder.hubby.models.dto.participations.GetResponseParticipationEvent;
import com.hobbyFinder.hubby.models.dto.participations.GetResponseParticipationsUser;
import com.hobbyFinder.hubby.models.dto.participations.ParticipationDto;
import com.hobbyFinder.hubby.models.dto.participations.UpdateParticipationDto;
import com.hobbyFinder.hubby.models.enums.UserParticipation;
import com.hobbyFinder.hubby.services.IServices.ParticipationInterface;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class ParticipationController {

    private ParticipationInterface participationInterface;

    @GetMapping(ParticipationRoutes.GET_ALL_EVENT_PARTICIPATIONS)
    public ResponseEntity<Page<GetResponseParticipationEvent>> getAllParticipations
            (@PathVariable UUID id, @RequestParam(defaultValue = "20") int size, @RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(participationInterface.getParticipationEvents(id, pageable));

    }

    @DeleteMapping(ParticipationRoutes.EXPEL_USER_FROM_EVENT)
    public ResponseEntity<Void> deleteUserParticipationFromEvent(@PathVariable UUID idEvent, @PathVariable UUID idParticipation) {
        participationInterface.deleteUserFromEvent(idEvent, idParticipation);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PutMapping(ParticipationRoutes.PARTICIPATION_MANAGEMENT)
    public ResponseEntity<UpdateParticipationDto> updateParticipationManagement(
            @PathVariable UUID idEvent,
            @PathVariable UUID idParticipation,
            @RequestBody @Valid UpdateParticipationDto updateParticipationDto) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.participationInterface.participationManagement(idEvent, idParticipation, updateParticipationDto));

    }

    //deleta sua própria participacao
    @DeleteMapping(ParticipationRoutes.USER_DELETE_PARTICIPATION)
    public ResponseEntity<Void> userDeleteParticipation(@PathVariable UUID eventId, @PathVariable UUID participationId) {
        ParticipationDto participationDto = new ParticipationDto(eventId, participationId);
        participationInterface.selfDeleteUserFromEvent(participationDto);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PutMapping(ParticipationRoutes.USER_UPDATE_PARTICIPATION)
    public ResponseEntity<Void> userUpdateParticipation(
            @PathVariable UUID eventId, @PathVariable UUID participationId,
            @RequestParam UserParticipation userParticipation) {
        participationInterface.updateParticipation(eventId, participationId, userParticipation);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping(ParticipationRoutes.GET_ALL_USER_PARTICIPATIONS)
    public ResponseEntity<Page<GetResponseParticipationsUser>> getAllParticipationsUser(
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(participationInterface.getParticipationsUser(pageable));
    }

}
