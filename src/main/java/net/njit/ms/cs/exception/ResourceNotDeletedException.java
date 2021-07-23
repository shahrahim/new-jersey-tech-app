package net.njit.ms.cs.exception;

public class ResourceNotDeletedException extends RuntimeException {

    public ResourceNotDeletedException() {
        super();
    }

    public ResourceNotDeletedException(final String message) {
        super(message);
    }

    public ResourceNotDeletedException(final Throwable cause) {
        super(cause);
    }

    public ResourceNotDeletedException(final String message, final Throwable cause) {
        super(message, cause);
    }
}