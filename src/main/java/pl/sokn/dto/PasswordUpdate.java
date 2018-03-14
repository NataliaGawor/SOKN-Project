package pl.sokn.dto;

import lombok.Data;
import pl.sokn.annotation.validation.PasswordMatches;

import javax.validation.constraints.NotBlank;

@Data
@PasswordMatches
public class PasswordUpdate implements PasswordBase {

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String password;
    @NotBlank
    private String matchingPassword;

    public PasswordUpdate() {
        super();
    }
}
