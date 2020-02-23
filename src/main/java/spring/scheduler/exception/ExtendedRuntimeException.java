package spring.scheduler.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class ExtendedRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private ExceptionKind anError;
    private Optional<Exception> originalException;

    public ExtendedRuntimeException(ExceptionKind anError) {
        this.anError = anError;
        this.originalException = Optional.empty();
    }

    public ExtendedRuntimeException(ExceptionKind anError, Exception originalException) {
        this.anError = anError;
        this.originalException = Optional.of(originalException);
    }
}