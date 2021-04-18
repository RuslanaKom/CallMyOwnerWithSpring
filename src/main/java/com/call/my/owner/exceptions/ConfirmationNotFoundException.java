package com.call.my.owner.exceptions;

public class ConfirmationNotFoundException extends Exception {

    private static final String MESSAGE = "No unconfirmed email found";

    public ConfirmationNotFoundException() {
        super(MESSAGE, null, false, false);
    }
}
