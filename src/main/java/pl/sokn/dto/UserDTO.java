package pl.sokn.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import pl.sokn.annotation.validation.PasswordMatches;
import pl.sokn.enums.Gender;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

/**
 * We use DTO classes for sending and retrieving data through HTTP protocol
 *
 * @apiNote @Data creates setters and getters
 * @apiNote @Builder used for builder design pattern
 * @apiNote @AllArgsConstructor creates constructor with all arguments
 * @apiNote @PasswordMatches - annotation created by
 *              @author Patryk that checks if passwords sent in json are the same
 * @apiNote @JsonInclude with this settings we exclude empty collections from json response
 * @apiNote @ApiModel model description for Swagger
 */
@Data
@Builder
@AllArgsConstructor
@PasswordMatches
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel(value = "User")
public class UserDTO implements Serializable {
    private static final long serialVersionUID = -5556990783870625832L;

    private Long id;

    /**
     * @apiNote - @NotBlank annotation is used when @Valid annotation is used in the controller. For example
     * @see pl.sokn.controller.RegistrationController#registerUser(UserDTO, HttpServletRequest)
     *
     * For more validation annotations
     * @see javax.validation.constraints package
     */
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

    @ApiModelProperty(required = true, position = 8, example = "single")
    private String affiliation;

    @ApiModelProperty(required = true, position = 9, example = "Kraków")
    private String city;

    @ApiModelProperty(required = true, position = 10, example = "31-111")
    private String zipCode;

    @ApiModelProperty(required = true, position = 11, example = "Poland")
    private String country;

    private Boolean enabled;
    private Set<AuthorityDTO> authorities;

    public UserDTO() {
        this.enabled = false;
        this.authorities = newHashSet();
    }
}
