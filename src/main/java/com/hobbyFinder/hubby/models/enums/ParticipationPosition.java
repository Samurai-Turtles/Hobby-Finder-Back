package com.hobbyFinder.hubby.models.enums;

import lombok.Getter;

@Getter
public enum ParticipationPosition {

    PARTICIPANT(0),
    ADMIN(1),
    CREATOR(2);

    private int rank;

    ParticipationPosition(int rank) {
        this.rank = rank;
    }

}
