package org.tms.travel_agency.exception;

public class DuplicateHotelException extends RuntimeException{
    public DuplicateHotelException() {
        super();
    }

    public DuplicateHotelException(String message) {
        super(message);
    }

    public DuplicateHotelException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateHotelException(Throwable cause) {
        super(cause);
    }
}
