package pl.sokn.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Builder
public class EmailMessage implements Serializable {
    private static final long serialVersionUID = -4426738819020514776L;
    @NotBlank
    private String email;
    @NotBlank
    private String name;
    @NotBlank
    private String text;
}
