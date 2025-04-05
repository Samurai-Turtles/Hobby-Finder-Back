package com.hobbyFinder.hubby.services.IServices;

import com.hobbyFinder.hubby.models.dto.evaluations.PostEvaluationDto;
import com.hobbyFinder.hubby.models.dto.evaluations.ResponseEvaluationDto;
import com.hobbyFinder.hubby.models.dto.situations.UserRateSituationDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

public interface EvaluationInterface {
    ResponseEvaluationDto evaluateEvent(UUID idEvent, PostEvaluationDto postAvaliationDTO, LocalDateTime requestTime);
    Collection<ResponseEvaluationDto> getEventAvaliations(UUID idEvent);

    /**
     * Checa se o usuário já avaliou o evento ao qual ele participou.
     * @param idParticipation - id da participação do usuário em um evento
     * @return um dto contendo "NOT_RATED" para não avaliado, "ALREADY_RATED" para já avaliado.
     */
    UserRateSituationDto hasUserAlreadyRatedTheEvent(UUID idParticipation);
}
