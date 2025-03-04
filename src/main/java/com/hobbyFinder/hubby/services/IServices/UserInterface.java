package com.hobbyFinder.hubby.services.IServices;

import com.hobbyFinder.hubby.models.dto.user.UserDTO;

import java.util.UUID;

public interface UserInterface {

    UserDTO getUser(UUID uuid);
}
