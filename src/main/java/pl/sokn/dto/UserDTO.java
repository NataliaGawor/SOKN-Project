package pl.sokn.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import pl.sokn.annotation.validation.PasswordMatches;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

@Data
@Builder
@AllArgsConstructor
@PasswordMatches
public class UserDTO implements Serializable {
    private static final long serialVersionUID = -5556990783870625832L;

    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String email;

    @NotBlank
    private String password;
    private String matchingPassword;
    private Boolean enabled;
    private Set<AuthorityDTO> authorities;

    public UserDTO() {
        this.enabled = false;
        this.authorities = newHashSet();
    }
}
