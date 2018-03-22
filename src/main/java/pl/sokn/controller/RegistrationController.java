package pl.sokn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sokn.definitions.SoknDefinitions.ApiMessages;
import pl.sokn.dto.CustomResponseMessage;
import pl.sokn.dto.PasswordCreate;
import pl.sokn.dto.PasswordUpdate;
import pl.sokn.dto.UserDTO;
import pl.sokn.entity.User;
import pl.sokn.exception.OperationException;
import pl.sokn.security.information.AuthenticationFacade;
import pl.sokn.service.RegistrationService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Api(tags = "Registration")
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

    @ApiOperation(value = "User registration")
    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity registerUser(@RequestBody @Valid final UserDTO user, final HttpServletRequest request) throws OperationException {
        final User registered = registrationService.save(convertToEntity(user));

        final String json = registrationService.createVerificationToken(registered, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(json);
    }

    @ApiOperation(value = "Resend the registration token")
    @PostMapping(value = "/resendRegistrationToken/{token}")
    public ResponseEntity resendRegistrationToken(@PathVariable("token") final String existingToken, HttpServletRequest request) throws OperationException {
        final String json = registrationService.generateNewVerificationToken(existingToken, request);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(json);
    }

    @ApiOperation("Registration confirmation after clicking in the link in the email - enables user")
    @GetMapping(value = "/registrationConfirm/{token}")
    public ResponseEntity confirmRegistration(@PathVariable final String token) throws OperationException {
        registrationService.enableRegisteredUser(token);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new CustomResponseMessage<>(HttpStatus.ACCEPTED, ApiMessages.EMAIL_CONFIRMED)
        );
    }

    @ApiOperation("Executes after clicking \"Forgot password\" option")
    @PostMapping(value = "/forgotPassword/{email:.+}")
    public ResponseEntity sendForgotPasswordEmail(@PathVariable final String email, final HttpServletRequest request) {
        final String json = registrationService.createPasswordResetTokenForUser(email, request);

        return ResponseEntity.ok(json);
    }

    @ApiOperation(value = "Resend the forgot password token")
    @PostMapping(value = "/resendPasswordToken/{token}")
    public ResponseEntity resendForgotPasswordToken(@PathVariable("token") final String existingToken, HttpServletRequest request) throws OperationException {
        final String json = registrationService.generateNewResetPasswordToken(existingToken, request);

        return ResponseEntity.ok(json);
    }

    @ApiOperation(value = "Redirect user to \"Provide new password\" page when response is ACCEPTED after clicking the link in the email")
    @GetMapping(value = "/changePassword/{id}/{token}")
    public ResponseEntity validatePasswordResetToken(@PathVariable("id") final Long id, @PathVariable("token") final String token) throws OperationException {
        registrationService.validatePasswordResetToken(id, token);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new CustomResponseMessage<>(HttpStatus.ACCEPTED, ApiMessages.TOKEN_IS_VALID));
    }

    @ApiOperation(value = "Option available on \"Provide new password\" page. User enters a new password")
    @PostMapping(value = "/resetPassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity resetPassword(@RequestBody @Valid final PasswordCreate passwordCreate) throws OperationException {
        registrationService.resetUserPassword(passwordCreate);

        return ResponseEntity.ok().body(new CustomResponseMessage<>(HttpStatus.OK, ApiMessages.P_UPDATED_SUCCESSFULLY));
    }

    @ApiOperation(value = "Option available for logged in user who just want update a password")
    @PostMapping(value = "/updatePassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity changeUserPassword(@RequestBody @Valid final PasswordUpdate passwordUpdate) throws OperationException {
        final String NAME = authenticationFacade.getAuthentication().getName();

        registrationService.changeUserPassword(passwordUpdate, NAME);
        return ResponseEntity.ok(new CustomResponseMessage<>(HttpStatus.ACCEPTED, ApiMessages.P_UPDATED_SUCCESSFULLY));
    }

    private UserDTO convertToDTO(final User user) {
        return User.convertTo(user);
    }

    private User convertToEntity(final UserDTO user) {
        return User.convertFrom(user);
    }
}
