package org.example.exceptions;

public class NonexistentIdException extends Exception {

    public NonexistentIdException(String message) {
        super(message);
    }

    public NonexistentIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonexistentIdException(Throwable cause) {
        super(cause);
    }
}
