package org.tms.travel_agency.exception;

public class NoSuchRoomException extends RuntimeException {
    public NoSuchRoomException() {
    }

    public NoSuchRoomException(String message) {
        super(message);
    }

    public NoSuchRoomException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchRoomException(Throwable cause) {
        super(cause);
    }
}
