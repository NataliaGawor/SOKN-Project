package pl.sokn.dto;

import lombok.Data;
import pl.sokn.annotation.validation.PasswordMatches;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@PasswordMatches
public class PasswordCreate implements PasswordBase {

        @NotBlank
        private String password;
        @NotBlank
        private String matchingPassword;
        @NotNull
        private Long userId;
}
