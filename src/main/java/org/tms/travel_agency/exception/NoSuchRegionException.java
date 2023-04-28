package org.tms.travel_agency.exception;

public class NoSuchRegionException extends RuntimeException {
    public NoSuchRegionException() {
        super();
    }

    public NoSuchRegionException(String message) {
        super(message);
    }

    public NoSuchRegionException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchRegionException(Throwable cause) {
        super(cause);
    }
}
