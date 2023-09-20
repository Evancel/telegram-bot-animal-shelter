package com.application.animalshelter.exception;

public class ShelterNotFoundException extends RuntimeException{
    public ShelterNotFoundException() {
    }

    public ShelterNotFoundException(String message) {
        super(message);
    }

    public ShelterNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShelterNotFoundException(Throwable cause) {
        super(cause);
    }

    public ShelterNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
