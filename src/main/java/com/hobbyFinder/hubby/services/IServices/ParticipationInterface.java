package com.hobbyFinder.hubby.services.IServices;

import com.hobbyFinder.hubby.models.dto.avaliations.PostAvaliationDto;
import com.hobbyFinder.hubby.models.dto.avaliations.ResponseAvaliationDto;
import com.hobbyFinder.hubby.models.dto.participations.GetResponseParticipationEvent;
import com.hobbyFinder.hubby.models.dto.participations.GetResponseParticipationsUser;
import com.hobbyFinder.hubby.models.dto.participations.ParticipationDto;
import com.hobbyFinder.hubby.models.dto.participations.UpdateParticipationDto;
import com.hobbyFinder.hubby.models.entities.Participation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

public interface ParticipationInterface {

    void selfDeleteUserFromEvent(ParticipationDto participationDTO);
    Participation findParticipation(UUID participationId);
    void removeParticipation(UUID participationId);
    void updateParticipation(UpdateParticipationDto updateParticipationDTO);
    void deleteUserFromEvent(UUID idEvent, UUID idParticipation);
    Page<GetResponseParticipationsUser> getParticipationsUser(Pageable pageable);
    Page<GetResponseParticipationEvent> getParticipationEvents(UUID idEvent, Pageable pageable);

    ResponseAvaliationDto evaluateEvent(UUID idEvent, PostAvaliationDto postAvaliationDTO, LocalDateTime requestTime);
    Collection<ResponseAvaliationDto> getEventAvaliations(UUID idEvent);
}
