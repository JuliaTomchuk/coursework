package org.tms.travel_agency.exception;

public class DuplicateRegionException extends RuntimeException{
    public DuplicateRegionException() {
        super();
    }

    public DuplicateRegionException(String message) {
        super(message);
    }

    public DuplicateRegionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateRegionException(Throwable cause) {
        super(cause);
    }
}
