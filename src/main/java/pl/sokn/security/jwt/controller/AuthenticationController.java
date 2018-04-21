package pl.sokn.security.jwt.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import pl.sokn.security.jwt.model.AuthenticationRequest;
import pl.sokn.security.jwt.model.JwtAuthenticationResponse;
import pl.sokn.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Api(tags = "Authentication")
@RestController
@RequestMapping
public class AuthenticationController {

    @Value("${jwt.header}")
    private String tokenHeader;

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuthenticationController(@pl.sokn.annotation.qualifier.UserService UserService userService,
                                    AuthenticationManager authenticationManager,
                                    JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @ApiOperation(value = "Enter valid credentials and get authentication token")
    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createAuthenticationToken(@Valid @RequestBody final AuthenticationRequest authenticationRequest) {

        // Perform the security
        // It uses CustomUserDetailService
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );

        // if user provided valid credentials we set him in the SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // generate JWT token for user (will be stored in localStorage)
        final String token = jwtTokenUtil.generateToken((UserDetails) authentication.getPrincipal());
        // it is optional for now, if we won't use information about user in the future, we can remove this
        final User user = userService.retrieveByEmail(authenticationRequest.getEmail());
        return ResponseEntity.ok(new JwtAuthenticationResponse(token, user));
    }

    /**
     * @param request with Authentication token that will be updated
     * @return new JWT token
     */
    @ApiOperation(value = "Refresh your token if is still valid")
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
        final User user = userService.retrieveByEmail(jwtTokenUtil.getUsernameFromToken(token));
        return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken, user));
    }
}
