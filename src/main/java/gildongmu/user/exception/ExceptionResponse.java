package gildongmu.user.exception;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
public class ExceptionResponse<T> {
    private final String code;
    private final T message;
    private final LocalDateTime timestamp;

    public ExceptionResponse(String code, T message) {
        this.code = code;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
