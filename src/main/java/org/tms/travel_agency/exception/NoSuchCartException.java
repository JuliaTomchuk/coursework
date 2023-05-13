package org.tms.travel_agency.exception;

public class NoSuchCartException extends RuntimeException {
    public NoSuchCartException() {
    }

    public NoSuchCartException(String message) {
        super(message);
    }

    public NoSuchCartException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchCartException(Throwable cause) {
        super(cause);
    }
}
