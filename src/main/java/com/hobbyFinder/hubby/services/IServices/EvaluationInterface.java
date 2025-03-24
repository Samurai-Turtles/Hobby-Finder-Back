package com.hobbyFinder.hubby.services.IServices;

import com.hobbyFinder.hubby.models.dto.evaluations.PostEvaluationDto;
import com.hobbyFinder.hubby.models.dto.evaluations.ResponseEvaluationDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

public interface EvaluationInterface {
    ResponseEvaluationDto evaluateEvent(UUID idEvent, PostEvaluationDto postAvaliationDTO, LocalDateTime requestTime);
    Collection<ResponseEvaluationDto> getEventAvaliations(UUID idEvent);
}
