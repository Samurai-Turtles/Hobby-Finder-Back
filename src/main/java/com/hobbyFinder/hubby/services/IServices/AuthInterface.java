package com.hobbyFinder.hubby.services.IServices;

import com.hobbyFinder.hubby.exception.AuthException.Login.CredenciaisLoginException;
import com.hobbyFinder.hubby.exception.AuthException.Registro.CredenciaisRegistroException;
import com.hobbyFinder.hubby.models.dto.user.AuthDTO;
import com.hobbyFinder.hubby.models.dto.user.LoginResponseDTO;
import com.hobbyFinder.hubby.models.dto.user.RegisterDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthInterface {

    void registroUsuario(RegisterDTO registerDTO);
    LoginResponseDTO loginUsuario(AuthDTO authDTO);
    void logoutUsuario(HttpServletRequest request);
}
