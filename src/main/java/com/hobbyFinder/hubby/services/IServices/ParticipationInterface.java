package com.hobbyFinder.hubby.services.IServices;

import com.hobbyFinder.hubby.models.dto.participations.GetResponseParticipationEvent;
import com.hobbyFinder.hubby.models.dto.participations.GetResponseParticipationsUser;
import com.hobbyFinder.hubby.models.dto.participations.ParticipationDto;
import com.hobbyFinder.hubby.models.dto.participations.UpdateParticipationDto;
import com.hobbyFinder.hubby.models.entities.Avaliation;
import com.hobbyFinder.hubby.models.entities.Participation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ParticipationInterface {

    void deleteUserFromEvent(ParticipationDto participationDTO);
    Participation findParticipation(UUID participationId);
    void removeParticipation(UUID participationId);
    void updateParticipation(UpdateParticipationDto updateParticipationDTO);
    void deleteUserFromEvent(UUID idEvent, UUID idParticipation);
    Page<GetResponseParticipationsUser> getParticipationsUser(Pageable pageable);
    Page<GetResponseParticipationEvent> getParticipationEvents(UUID idEvent, Pageable pageable);

    List<Avaliation> getAvaliationsFromEvent(UUID idEvent);

    void saveParticipation(Participation participation);

    double getAvgStarsByEvent(UUID idEvent);
    double getAvgStarsByUser(UUID idUser);
}
