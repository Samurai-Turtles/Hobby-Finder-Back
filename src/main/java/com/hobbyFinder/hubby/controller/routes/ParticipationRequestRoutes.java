package com.hobbyFinder.hubby.controller.routes;

public class ParticipationRequestRoutes {

    public static final String POST_REQUEST = EventRoutes.EVENT_BASE + "/{targetEventId}/request";
    public static final String GET_REQUESTS_BY_EVENT = EventRoutes.EVENT_BASE + "/{targetEventId}/request";
    public static final String GET_REQUESTS_BY_USER = UserRoutes.USER_BASE + "/request";
    public static final String ACCEPT_REQUEST = EventRoutes.EVENT_BASE + "/{targetEventId}/request/{targetRequestId}";
    public static final String DELETE_REQUEST = EventRoutes.EVENT_BASE + "/{targetEventId}/request/{targetRequestId}";
    public static final String DELETE_REQUEST_BY_USER = UserRoutes.USER_BASE + "/request/{targetRequestId}";

}
