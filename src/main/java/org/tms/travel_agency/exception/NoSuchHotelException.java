package org.tms.travel_agency.exception;

public class NoSuchHotelException extends RuntimeException{
    public NoSuchHotelException() {
        super();
    }

    public NoSuchHotelException(String message) {
        super(message);
    }

    public NoSuchHotelException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchHotelException(Throwable cause) {
        super(cause);
    }
}
