package com.call.my.owner.exceptions;

public class DuplicateStuffNameException extends Exception {
    private static final String message = "Stuff with this name already exists";

    public DuplicateStuffNameException() {
        super(message);
    }
}
