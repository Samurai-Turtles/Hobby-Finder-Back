package com.hobbyFinder.hubby.services.IServices;

import com.hobbyFinder.hubby.models.dto.avaliations.PostAvaliationDto;
import com.hobbyFinder.hubby.models.dto.avaliations.ResponseAvaliationDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

public interface EvaluationInterface {
    ResponseAvaliationDto evaluateEvent(UUID idEvent, PostAvaliationDto postAvaliationDTO, LocalDateTime requestTime);
    Collection<ResponseAvaliationDto> getEventAvaliations(UUID idEvent);
}
