package pl.sokn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sokn.definitions.SoknDefinitions.ApiMessages;
import pl.sokn.dto.CustomResponseMessage;
import pl.sokn.dto.UserDTO;
import pl.sokn.entity.User;
import pl.sokn.exception.OperationException;
import pl.sokn.security.information.AuthenticationFacade;
import pl.sokn.service.RegistrationService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class RegistrationController {

    private final RegistrationService registrationService;
    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public RegistrationController(RegistrationService registrationService,
                                  AuthenticationFacade authenticationFacade) {
        this.registrationService = registrationService;
        this.authenticationFacade = authenticationFacade;
    }

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity registerUser(@RequestBody @Valid final UserDTO user, final HttpServletRequest request) throws OperationException {
        final User registered = registrationService.save(convertToEntity(user));

        final String json = registrationService.createVerificationToken(registered, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(json);
    }

    @PostMapping(value = "/resendRegistrationToken/{token}")
    public ResponseEntity resendRegistrationToken(@PathVariable("token") final String existingToken, HttpServletRequest request) throws OperationException {
        final String json = registrationService.generateNewVerificationToken(existingToken, request);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(json);
    }

    @GetMapping(value = "/registrationConfirm/{token}")
    public ResponseEntity confirmRegistration(@PathVariable final String token) throws OperationException {
        registrationService.enableRegisteredUser(token);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new CustomResponseMessage<>(HttpStatus.ACCEPTED, ApiMessages.EMAIL_CONFIRMED)
        );
    }

    protected UserDTO convertToDTO(final User user) {
        return User.convertTo(user);
    }

    protected User convertToEntity(final UserDTO user) {
        return User.convertFrom(user);
    }
}
