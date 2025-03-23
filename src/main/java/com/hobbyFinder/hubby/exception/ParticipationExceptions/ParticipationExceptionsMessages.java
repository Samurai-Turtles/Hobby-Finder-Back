package com.hobbyFinder.hubby.exception.ParticipationExceptions;

public class ParticipationExceptionsMessages {

    public final static String INCORRECT_EVENT_ID = "O id do evento especificado não é igual ao dado na participação.";
    public final static String USER_REQUISITION_DENIED = "O usuário não está presente no evento.";
    public final static String USER_ALREADY_PARTICIPATE = "O usuário já está presente no evento.";
    public final static String INVALID_USER_POSITION = "O usuário não possui cargo para essa ação.";
    public final static String USER_POSITION_DENIED = "Cargo do usuário não é superior ao cargo do qual está realizar a operação.";
    public final static String SELF_DELETE_ID_DENIED = "O id do usuário não corresponde ao id da participação.";
}
