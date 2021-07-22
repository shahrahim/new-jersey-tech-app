package net.njit.ms.cs.exception;

public class ResourceNotCreatedException extends RuntimeException {

    public ResourceNotCreatedException() {
        super();
    }

    public ResourceNotCreatedException(final String message) {
        super(message);
    }

    public ResourceNotCreatedException(final Throwable cause) {
        super(cause);
    }

    public ResourceNotCreatedException(final String message, final Throwable cause) {
        super(message, cause);
    }
}