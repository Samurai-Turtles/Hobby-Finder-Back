package com.hobbyFinder.hubby.services.IServices;

import com.hobbyFinder.hubby.exception.HubbyException;
import com.hobbyFinder.hubby.exception.NotFound.UserNotFoundException;
import com.hobbyFinder.hubby.models.dto.user.UserDTO;
import com.hobbyFinder.hubby.models.dto.user.UserPutDTO;
import com.hobbyFinder.hubby.models.dto.user.UserResponseDTO;

import java.util.UUID;

public interface UserInterface {

    UserResponseDTO getUser(UUID uuid) throws UserNotFoundException;
    void deleteUser();
    UserDTO updateUser(UserPutDTO userDTO) throws HubbyException;
}
