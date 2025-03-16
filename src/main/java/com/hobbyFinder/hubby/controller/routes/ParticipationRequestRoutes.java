package com.hobbyFinder.hubby.controller.routes;

public class ParticipationRequestRoutes {

    public static final String POST_REQUEST = BaseRoutes.BASE + "/event/{targetEventId}/request";
    public static final String GET_REQUESTS_BY_EVENT = BaseRoutes.BASE + "/event/{targetEventId}/request";
    public static final String GET_REQUESTS_BY_USER = BaseRoutes.BASE + "/user/request";
    public static final String ACCEPT_REQUEST = BaseRoutes.BASE + "/event/{targetEventId}/request/{targetRequestId}";
    public static final String DELETE_REQUEST = BaseRoutes.BASE + "/event/{targetEventId}/request/{targetRequestId}";
    public static final String DELETE_REQUEST_BY_USER = BaseRoutes.BASE + "/user/request/{targetRequestId}";

}
