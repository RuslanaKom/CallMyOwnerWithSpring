package com.call.my.owner.exceptions;

public class ConfirmationNotFoundException extends Exception {

    private static final String message = "No unconfirmed email found";

    public ConfirmationNotFoundException() {
        super(message);
    }
}
