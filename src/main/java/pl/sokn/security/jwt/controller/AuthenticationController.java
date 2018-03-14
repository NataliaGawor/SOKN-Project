package pl.sokn.security.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import pl.sokn.entity.User;
import pl.sokn.security.jwt.JwtTokenUtil;
import pl.sokn.security.jwt.model.JwtAuthenticationRequest;
import pl.sokn.security.jwt.model.JwtAuthenticationResponse;
import pl.sokn.service.UserService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping
public class AuthenticationController {

    @Value("${jwt.header}")
    private String tokenHeader;

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuthenticationController(UserService userService,
                                    AuthenticationManager authenticationManager,
                                    JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) {

        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String token = jwtTokenUtil.generateToken((UserDetails) authentication.getPrincipal());
        final User user = userService.retrieveByCredentials(authenticationRequest.getUsername());

        return ResponseEntity.ok(new JwtAuthenticationResponse(token, user));
    }

    @GetMapping(path = "/refresh")
    public ResponseEntity refreshAndGetAuthenticationToken(HttpServletRequest request) {
        final String authToken = request.getHeader(tokenHeader);
        if (authToken == null) {
            return ResponseEntity.badRequest().build();
        }

        final String token = authToken.substring(7);

        // this method throws MalformedJwtException and ExpiredJwtException if needed
        jwtTokenUtil.canTokenBeRefreshed(token);

        final String refreshedToken = jwtTokenUtil.refreshToken(token);
        final User user = userService.retrieveByCredentials(jwtTokenUtil.getUsernameFromToken(token));
        return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken, user));
    }
}
