package com.hobbyFinder.hubby.services.IServices;

import com.hobbyFinder.hubby.models.dto.user.AuthDTO;
import com.hobbyFinder.hubby.models.dto.user.LoginResponseDTO;
import com.hobbyFinder.hubby.models.dto.user.RegisterDTO;

public interface AuthInterface {

    void registroUsuario(RegisterDTO registerDTO);
    LoginResponseDTO loginUsuario(AuthDTO authDTO);
}
