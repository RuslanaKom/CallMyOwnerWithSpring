package com.call.my.owner.exceptions;

public class UserNotFoundException extends Exception {

    private static final String MESSAGE = "No user found.";

    public UserNotFoundException() {
        super(MESSAGE, null, false, false);
    }
}
