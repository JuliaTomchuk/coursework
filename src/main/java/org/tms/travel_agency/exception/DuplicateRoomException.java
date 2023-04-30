package org.tms.travel_agency.exception;

public class DuplicateRoomException extends RuntimeException {
    public DuplicateRoomException() {
        super();
    }

    public DuplicateRoomException(String message) {
        super(message);
    }

    public DuplicateRoomException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateRoomException(Throwable cause) {
        super(cause);
    }
}
