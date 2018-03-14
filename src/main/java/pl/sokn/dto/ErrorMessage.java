package pl.sokn.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;
import pl.sokn.exception.OperationException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Data
public class ErrorMessage {

    private Class<? extends OperationException> aClass;
    private HttpStatus status;
    private List<String> errors;

    public ErrorMessage(HttpStatus status, List<String> errors) {
        this.status = status;
        this.errors = errors;
    }

    public ErrorMessage(String... errors) {
        this.errors = Arrays.asList(errors);
    }

    public ErrorMessage(HttpStatus status, String error) {
        this(status, Collections.singletonList(error));
    }

    public ErrorMessage(HttpStatus status, String ... errors) {
        this(status, Arrays.asList(errors));
    }

    public ErrorMessage(Class<? extends OperationException> aClass, HttpStatus status, List<String> errors) {
        this(status, errors);
        this.aClass = aClass;
    }
}
