package com.hobbyFinder.hubby.services.IServices;

import com.hobbyFinder.hubby.models.dto.events.ParticipationDto;

public interface ParticipationInterface {

    void deleteUserFromEvent(ParticipationDto participationDTO);
}
