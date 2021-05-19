package com.ozeeesoftware.springsecurityjwt.exception.model;

public class UsernameExistsException extends Exception{

    public UsernameExistsException(String message) {
        super(message);
    }
}
