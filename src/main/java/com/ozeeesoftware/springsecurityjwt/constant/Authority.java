package com.ozeeesoftware.springsecurityjwt.constant;

public class Authority {

    public static final String[] USER_AUTHORITIES={"user:read"};
    public static final String[] MANAGER_AUTHORITIES={"user:read","user:update","post:approve","post:delete"};
    public static final String[] ADMIN_AUTHORITIES={"user:read","user:update","user:create","user:delete","post:approve","post:delete"};

}
