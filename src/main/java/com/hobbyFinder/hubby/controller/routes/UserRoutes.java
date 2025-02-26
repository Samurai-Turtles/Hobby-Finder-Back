package com.hobbyFinder.hubby.controller.routes;

public class UserRoutes {
    public static final String BASE = BaseRoutes.BASE + "/user";
    public static final String POST_USER = BASE;
    public static final String PUT_AUTH_USER = BASE;
    public static final String GET_USER_BY_LOCATION = BASE;
    public static final String GET_USER_BY_ID = BASE + "/{id}";
    public static final String PUT_USER_BY_ID = BASE + "/{id}";
    public static final String DELETE =  BASE + "/delete";
    public static final String LOGIN =  BASE + "/login";
    public static final String LOGOUT = BASE + "/logout";
    public static final String RECOVER_PASSOWRD = BASE + "/recover-password";
    public static final String PROFILE_PHOTO = BASE + "/profile-photo";
}
