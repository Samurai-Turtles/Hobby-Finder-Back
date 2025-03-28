package com.hobbyFinder.hubby.controller;

import com.hobbyFinder.hubby.controller.routes.SituationRoutes;
import com.hobbyFinder.hubby.models.dto.situations.SituationDto;
import com.hobbyFinder.hubby.services.IServices.SituationInterface;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class SituationController {

    private SituationInterface situationService;

    @GetMapping(SituationRoutes.SITUATION_BASE)
    public ResponseEntity<SituationDto> getSituationByAuthUser(UUID id) {
        return ResponseEntity.ok(
                situationService.getSituationByAuthUser(id));
    }

}
