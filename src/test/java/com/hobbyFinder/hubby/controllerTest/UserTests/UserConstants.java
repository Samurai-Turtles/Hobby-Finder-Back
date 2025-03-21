package com.hobbyFinder.hubby.controllerTest.UserTests;

import com.hobbyFinder.hubby.models.dto.user.AuthDTO;
import com.hobbyFinder.hubby.models.dto.user.RegisterDTO;

public class UserConstants {

    public static final String USER1_EMAIL = "victor@gmail.com";
    public static final String USER1_USERNAME = "victor";
    public static final String USER1_PASSWORD = "senha1234";
    public static final String USER1_FULL_NAME = "Victor Nome Inteiro";

    public static final String USER2_EMAIL = "gabriel@gmail.com";
    public static final String USER2_USERNAME = "gabriel";
    public static final String USER2_PASSWORD = "1234senha";
    public static final String USER2_FULL_NAME = "Gabriel Nome Inteiro";

    public static final String USERNAME_TAMANHO_INVALIDO = "lou";
    public static final String USERNAME_INVALIDO = "!#&$";

    public static final String EMAIL_NAO_UTILIZADO = "novoEmail@gmail.com";
    public static final String USERNAME_NAO_UTILIZADO = "username";
    public static final String PASSWORD_NAO_UTILIZADA = "senha123";
    public static final String FULL_NAME_NAO_UTILIZADO = "Nome Inteiro";

    public static final String LOGIN_INEXISTENTE = "esseUserNaoExiste@gmail.com";

    public static final RegisterDTO primeiroRegistroDto = new RegisterDTO(USER1_EMAIL, USER1_USERNAME, USER1_PASSWORD, USER1_FULL_NAME);
    public static final AuthDTO primeiroAuthDto = new AuthDTO(USER1_EMAIL, USER1_PASSWORD);

    public static final RegisterDTO segundoRegistroDto = new RegisterDTO(USER2_EMAIL, USER2_USERNAME, USER2_PASSWORD, USER2_FULL_NAME);
    public static final AuthDTO segundoAuthDto = new AuthDTO(USER2_USERNAME, USER2_PASSWORD);
}
