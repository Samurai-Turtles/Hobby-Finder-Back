package com.hobbyFinder.hubby.services.IServices;

import com.hobbyFinder.hubby.exception.HubbyException;
import com.hobbyFinder.hubby.models.dto.user.UserDTO;
import com.hobbyFinder.hubby.models.dto.user.UserPutDTO;

import java.util.UUID;

public interface UserInterface {

    UserDTO getUser(UUID uuid);
    void deleteUser();
    UserDTO updateUser(UserPutDTO userDTO) throws HubbyException;
}
