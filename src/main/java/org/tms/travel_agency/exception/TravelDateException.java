package org.tms.travel_agency.exception;

public class TravelDateException extends RuntimeException {
    public TravelDateException() {
    }

    public TravelDateException(String message) {
        super(message);
    }

    public TravelDateException(String message, Throwable cause) {
        super(message, cause);
    }

    public TravelDateException(Throwable cause) {
        super(cause);
    }
}
