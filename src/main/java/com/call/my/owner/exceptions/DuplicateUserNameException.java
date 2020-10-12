package com.call.my.owner.exceptions;

public class DuplicateUserNameException extends RuntimeException {
    private static final String message = "User with this name already exists";

    public DuplicateUserNameException() {
        super(message);
    }
}
