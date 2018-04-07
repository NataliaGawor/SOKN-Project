package pl.sokn.security.jwt.model;

import lombok.Data;
import pl.sokn.entity.Authority;
import pl.sokn.entity.User;

import java.io.Serializable;
import java.util.Set;

@Data
public class JwtAuthenticationResponse implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;

    private final String token;
    private final User user;
    private final Set<Authority>authorities;

    public JwtAuthenticationResponse(String token, User user, Set<Authority> authorities) {
        this.token = token;
        this.user = user;
        this.authorities = authorities;
    }
}
