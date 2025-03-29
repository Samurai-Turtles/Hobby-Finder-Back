package com.hobbyFinder.hubby.controller.routes;

public class EventRoutes {
    public static final String EVENT_BASE = BaseRoutes.BASE + "/event";
    public static final String POST_EVENT = EVENT_BASE;
    public static final String GET_EVENT_BY_AUTH_USER = EVENT_BASE;
    public static final String GET_EVENT_BY_USER_ID = EVENT_BASE + UserRoutes.USER_BASE + "/{userId}";
    public static final String GET_EVENT_BY_ID = EVENT_BASE + "/{id}";
    public static final String PUT_EVENT = EVENT_BASE + "/{id}";
    public static final String DELETE_EVENT = EVENT_BASE + "/{id}";
}
