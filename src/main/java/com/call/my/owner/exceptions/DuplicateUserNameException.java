package com.call.my.owner.exceptions;

public class DuplicateUserNameException extends Exception {
    private static final String message = "User with this name already exists";

    public DuplicateUserNameException() {
        super(message);
    }
}
