package com.call.my.owner.exceptions;

public class DuplicateStuffNameException extends Exception {
    private static final String MESSAGE = "Stuff with this name already exists";

    public DuplicateStuffNameException() {
        super(MESSAGE, null, false, false);
    }
}
