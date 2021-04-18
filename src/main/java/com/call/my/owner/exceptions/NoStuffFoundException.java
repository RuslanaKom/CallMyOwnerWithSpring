package com.call.my.owner.exceptions;

public class NoStuffFoundException extends Exception {

    public NoStuffFoundException(String message) {
        super(message, null, false, false);
    }
}
