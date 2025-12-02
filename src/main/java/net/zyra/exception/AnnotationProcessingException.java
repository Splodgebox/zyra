package net.zyra.exception;

public class AnnotationProcessingException extends RuntimeException {
    public AnnotationProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public AnnotationProcessingException(String message) {
        super(message);
    }
}
