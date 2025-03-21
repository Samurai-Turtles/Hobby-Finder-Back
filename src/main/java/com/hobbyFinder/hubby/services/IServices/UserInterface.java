package com.hobbyFinder.hubby.services.IServices;

import com.hobbyFinder.hubby.exception.HubbyException;
import com.hobbyFinder.hubby.exception.NotFound.UserNotFoundException;
import com.hobbyFinder.hubby.models.dto.events.GetParticipationsUser;
import com.hobbyFinder.hubby.models.dto.user.UserDTO;
import com.hobbyFinder.hubby.models.dto.user.UserPutDTO;
import com.hobbyFinder.hubby.models.dto.user.UserResponseDTO;
import com.hobbyFinder.hubby.models.entities.Participation;
import com.hobbyFinder.hubby.models.entities.User;

import java.util.List;
import java.util.UUID;

public interface UserInterface {

    UserResponseDTO getUserResponse(UUID uuid);
    User getUser(UUID uuid);
    void deleteUser();
    UserDTO updateUser(UserPutDTO userDTO);
    List<GetParticipationsUser> getParticipationsUser();
}
