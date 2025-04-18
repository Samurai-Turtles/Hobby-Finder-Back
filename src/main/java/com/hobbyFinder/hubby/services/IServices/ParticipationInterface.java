package com.hobbyFinder.hubby.services.IServices;

import com.hobbyFinder.hubby.models.dto.participations.*;
import com.hobbyFinder.hubby.models.entities.Participation;
import com.hobbyFinder.hubby.models.enums.UserParticipation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ParticipationInterface {

    void selfDeleteUserFromEvent(ParticipationDto participationDTO);
    Participation findParticipation(UUID participationId);
    void removeParticipation(UUID participationId);
    void updateParticipation(UUID idEvent, UUID idParticipation, UserParticipation userParticipation);
    void deleteUserFromEvent(UUID idEvent, UUID idParticipation);
    Page<GetResponseParticipationsUser> getParticipationsUser(Pageable pageable);
    Page<GetResponseParticipationEvent> getParticipationEvents(UUID idEvent, Pageable pageable);
    UpdateParticipationDto participationManagement(UUID idEvent, UUID idParticipation, UpdateParticipationDto updateParticipationDTO);
}
