package com.hobbyFinder.hubby.services.ServicesImpl;

import com.hobbyFinder.hubby.models.dto.events.ParticipationDto;
import com.hobbyFinder.hubby.services.IServices.ParticipationInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParticipationServiceImpl implements ParticipationInterface {

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @Override
    public void deleteUserFromEvent(ParticipationDto participationDTO) {

    }
}
