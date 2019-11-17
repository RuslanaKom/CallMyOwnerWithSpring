package com.call.my.owner.exceptions;

public class NoLoggedInUserException extends Exception {
    private static final String message = "No user is logged in";

    public NoLoggedInUserException() {
        super(message);
    }
}
