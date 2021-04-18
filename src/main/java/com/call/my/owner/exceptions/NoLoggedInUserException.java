package com.call.my.owner.exceptions;

public class NoLoggedInUserException extends Exception {
    private static final String MESSAGE = "No user is logged in";

    public NoLoggedInUserException() {
        super(MESSAGE, null, false, false);
    }
}
