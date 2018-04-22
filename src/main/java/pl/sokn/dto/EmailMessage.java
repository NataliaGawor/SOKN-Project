package pl.sokn.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel(value = "sendContactEmail")
public class EmailMessage implements Serializable {
    private static final long serialVersionUID = -4426738819020514776L;
    @NotBlank
    private String email;
    @NotBlank
    private String name;
    @NotBlank
    private String text;

}
