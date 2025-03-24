package com.hobbyFinder.hubby.controller.routes;

public class UserRoutes {
    public static final String USER_BASE = BaseRoutes.BASE + "/user";
    public static final String POST_USER = USER_BASE;
    public static final String PUT_AUTH_USER = USER_BASE;
    public static final String GET_USER_BY_ID = USER_BASE + "/{id}";
    public static final String DELETE =  USER_BASE + "/delete";
    public static final String LOGIN = USER_BASE + "/login";
    public static final String LOGOUT = USER_BASE + "/logout";
    public static final String RECOVER_PASSOWRD = USER_BASE + "/recover-password";
}
