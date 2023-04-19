package org.tms.travel_agency.exception;

public class UserWithThatUsernameAlreadyExistException extends RuntimeException{

    public UserWithThatUsernameAlreadyExistException() {
    }

    public UserWithThatUsernameAlreadyExistException(String message) {
        super(message);
    }

    public UserWithThatUsernameAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserWithThatUsernameAlreadyExistException(Throwable cause) {
        super(cause);
    }
}
