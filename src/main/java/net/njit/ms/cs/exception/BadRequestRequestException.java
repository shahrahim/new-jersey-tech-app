package net.njit.ms.cs.exception;

public class BadRequestRequestException extends RuntimeException {

    public BadRequestRequestException() {
        super();
    }

    public BadRequestRequestException(final String message) {
        super(message);
    }

    public BadRequestRequestException(final Throwable cause) {
        super(cause);
    }

    public BadRequestRequestException(final String message, final Throwable cause) {
        super(message, cause);
    }
}