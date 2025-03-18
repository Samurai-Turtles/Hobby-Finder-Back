package com.hobbyFinder.hubby.models.entities;

public enum UserParticipation {

    CONFIRMED_PRESENCE("presença confirmada."),
    UNCONFIRMED_PRESENCE("presença não confirmada");

    private String participationPresence;

    UserParticipation(String participationPresence) {
        this.participationPresence = participationPresence;
    }

}
