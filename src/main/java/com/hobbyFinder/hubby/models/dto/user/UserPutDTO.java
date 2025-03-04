package com.hobbyFinder.hubby.models.dto.user;

import com.hobbyFinder.hubby.models.enums.InterestEnum;

import java.util.List;

public record UserPutDTO(
        String email,
        String username,
        String password,
        String name,
        String bio,
        List<InterestEnum> interests) {
}