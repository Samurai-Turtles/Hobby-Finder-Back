package com.hobbyFinder.hubby.models.enums;

import lombok.Getter;

@Getter
public enum ParticipationPosition {

    CREATOR(3),
    ADMIN(2),
    PARTICIPANT(1);

    private int rank;

    ParticipationPosition(int rank) {
        this.rank = rank;
    }

}
