package com.hobbyFinder.hubby.controller.routes;

import org.springframework.security.core.parameters.P;

public class PhotoRoutes {

    public static final String PHOTO_BASE = BaseRoutes.BASE + "/photo";

    public static final String PHOTO_BY_USER_LOGGED = PHOTO_BASE;

    public static final String PHOTO_BY_EVENT = EventRoutes.GET_EVENT_BY_ID + "/photo";

    public static final String PHOTO_BY_ID = PHOTO_BASE + "/{id}";
}
