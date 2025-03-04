package com.hobbyFinder.hubby.controller.routes;

import com.hobbyFinder.hubby.models.entities.User;

public class EventRoutes {
    public static final String BASE = BaseRoutes.BASE + "/evento";
    public static final String POST_EVENT = BASE;
    public static final String GET_EVENT_BY_AUTH_USER = BASE;
    public static final String GET_EVENT_BY_USER_ID = BASE + UserRoutes.BASE + "/{id}";
    public static final String GET_EVENT_BY_ID = BASE + "/{id}";
    public static final String PUT_EVENT = BASE + "/{id}";
    public static final String DELETE_EVENT = BASE + "/{id}";
}
