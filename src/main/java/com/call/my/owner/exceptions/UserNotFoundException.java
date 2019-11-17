package com.call.my.owner.exceptions;

public class UserNotFoundException extends Exception {

    private static final String message = "No user found";

    public UserNotFoundException() {
        super(message);
    }
}
