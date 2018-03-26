package pl.sokn.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import pl.sokn.annotation.validation.PasswordMatches;
import pl.sokn.enums.Gender;

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
    @ApiModelProperty(required = true, position = 1, example = "firstName")
    private String firstName;

    @NotBlank
    @ApiModelProperty(required = true, position = 2, example = "lastName")
    private String lastName;

    @ApiModelProperty(required = true, position = 3, example = "MALE")
    private Gender gender;

    @NotBlank
    @ApiModelProperty(required = true, position = 4, example = "degree")
    private String degree;

    @NotBlank
    @ApiModelProperty(required = true, position = 5, example = "username@email.com")
    private String email;

    @NotBlank
    @ApiModelProperty(required = true, position = 6, example = "password")
    private String password;

    @ApiModelProperty(required = true, position = 7, example = "matching")
    private String matchingPassword;
    private Boolean enabled;
    private Set<AuthorityDTO> authorities;

    public UserDTO() {
        this.enabled = false;
        this.authorities = newHashSet();
    }
}
