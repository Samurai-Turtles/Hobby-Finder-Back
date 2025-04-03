package com.hobbyFinder.hubby.services.IServices;

import com.hobbyFinder.hubby.models.dto.situations.SituationDto;

import java.util.UUID;

public interface SituationInterface {

    SituationDto getSituationByAuthUser(UUID idEvent);

}
