package com.hobbyFinder.hubby.controller.routes;

public class ParticipationRoutes {
    public static final String GET_ALL_EVENT_PARTICIPATIONS = EventRoutes.EVENT_BASE + "/{id}" + "/participation";
    public static final String EXPEL_USER_FROM_EVENT = EventRoutes.EVENT_BASE + "/{idEvent}" + "/participation" + "/" + "{idParticipation}";
    public static final String PARTICIPATION_MANAGEMENT = EventRoutes.EVENT_BASE + "/{idEvent}" + "/participation" + "/{idParticipation}" + "/update-presence";
    public static final String USER_DELETE_PARTICIPATION = EventRoutes.EVENT_BASE + "/{eventId}" + "/participation" + "/{participationId}" + "/user-auth";
    public static final String USER_UPDATE_PARTICIPATION = EventRoutes.EVENT_BASE + "/{eventId}" + "/participation" + "/{participationId}";
    public static final String GET_ALL_USER_PARTICIPATIONS = UserRoutes.USER_BASE + "/participation";
}
