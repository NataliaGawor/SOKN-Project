package pl.sokn.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel(value = "User")
public class UserDTO implements Serializable {
    private static final long serialVersionUID = -5556990783870625832L;

    private Long id;

    @NotBlank
    @ApiModelProperty(required = true, position = 1, example = "username")
    private String username;

    @NotBlank
    @ApiModelProperty(required = true, position = 2, example = "username@email.com")
    private String email;

    @NotBlank
    @ApiModelProperty(required = true, position = 3, example = "password")
    private String password;
    @ApiModelProperty(required = true, position = 4, example = "matching")
    private String matchingPassword;
    private Boolean enabled;
    private Set<AuthorityDTO> authorities;

    public UserDTO() {
        this.enabled = false;
        this.authorities = newHashSet();
    }
}
