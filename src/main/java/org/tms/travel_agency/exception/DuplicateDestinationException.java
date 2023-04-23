package org.tms.travel_agency.exception;



public class DuplicateDestinationException extends RuntimeException{

    public DuplicateDestinationException() {
    }

    public DuplicateDestinationException(String message) {
        super(message);
    }

    public DuplicateDestinationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateDestinationException(Throwable cause) {
        super(cause);
    }
}
