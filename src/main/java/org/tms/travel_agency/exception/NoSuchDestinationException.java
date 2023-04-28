package org.tms.travel_agency.exception;

public class NoSuchDestinationException extends RuntimeException{
    public NoSuchDestinationException() {
        super();
    }

    public NoSuchDestinationException(String message) {
        super(message);
    }

    public NoSuchDestinationException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchDestinationException(Throwable cause) {
        super(cause);
    }
}
