package pl.sokn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class CustomResponseMessage<E> implements Serializable {
    private static final long serialVersionUID = -6996609131349035242L;
    private E object;
    private String message;
}
