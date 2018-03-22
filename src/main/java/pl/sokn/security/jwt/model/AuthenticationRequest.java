package pl.sokn.security.jwt.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class AuthenticationRequest implements Serializable {

    private static final long serialVersionUID = -8445943548965154778L;

    @ApiModelProperty(required = true, position = 1)
    private String username;
    @ApiModelProperty(required = true, position = 2)
    private String password;
}
