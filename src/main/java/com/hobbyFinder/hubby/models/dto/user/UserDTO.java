package com.hobbyFinder.hubby.models.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {


    private String login;
    private String password;
    private String role;

    public UserDTO(String login, String password, String role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

}

