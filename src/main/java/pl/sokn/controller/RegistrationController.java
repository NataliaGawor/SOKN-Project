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
import pl.sokn.entity.PasswordResetToken;
import pl.sokn.entity.User;
import pl.sokn.entity.VerificationToken;
import pl.sokn.exception.OperationException;
import pl.sokn.security.information.AuthenticationFacade;
import pl.sokn.service.RegistrationService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 *  Only this controller doesn't extends GenericController because we don't need CRUD operations here.
 *  For CRUD operation on User entity class:
 *  @see UserController
 *
 *  @apiNote - @Api Controller title in Swagger
 *  @apiNote - @RestController - thanks to that Spring knows it is controller class
 *  @apiNote - @RequestMapping - set path for every controller's method
 *                  produces - means that we return data as "application/json"
 */
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

    /**
     * End point for user's registration
     *
     * Set "consumes" param in @PostMapping annotations when you only accept data of one type
     * In this case "application/json"
     */
    @ApiOperation(value = "User registration")
    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity registerUser(@RequestBody @Valid final UserDTO user,
                                       final HttpServletRequest request) throws OperationException {
        // save user in database if is not already registered
        final User registered = registrationService.save(convertToEntity(user));

        // if registered successfully then create verification token and send an email
        final VerificationToken vToken = registrationService.createVerificationToken(registered, request);
        final String json = registrationService.sendRegistrationEmail(vToken, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(json);
    }

    /**
     * Resend registration email if needed
     * @param existingToken - token sent in first email
     * @param request - contains data like server IP address, port etc.
     * @return json with new token, user's email address etc.
     * @throws OperationException when token is invalid or expired
     *
     * @apiNote - @PathVariable allows as to handle the path with variables
     */
    @ApiOperation(value = "Resend the registration token")
    @PostMapping(value = "/resendRegistrationToken/{token}")
    public ResponseEntity resendRegistrationToken(@PathVariable("token") final String existingToken,
                                                  final HttpServletRequest request) throws OperationException {
        final VerificationToken vToken = registrationService.generateNewVerificationToken(existingToken, request);
        final String json = registrationService.sendRegistrationEmail(vToken, request);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(json);
    }

    /**
     *
     * @param token that was sent in email after registration
     * @return 201 status and confirmation message
     * @throws OperationException when token expired or is invalid
     */
    @ApiOperation("Registration confirmation after clicking in the link in the email - enables user")
    @GetMapping(value = "/registrationConfirm/{token}")
    public ResponseEntity confirmRegistration(@PathVariable final String token) throws OperationException {
        registrationService.enableRegisteredUser(token);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new CustomResponseMessage<>(HttpStatus.ACCEPTED, ApiMessages.EMAIL_CONFIRMED)
        );
    }

    /**
     *
     * @param email fulfilled by form on "forgot  password" page
     * @param request - IP address etc.
     * @return json with token, and some user's details
     */
    @ApiOperation("Executes after clicking \"Forgot password\" option")
    @PostMapping(value = "/forgotPassword/{email:.+}")
    public ResponseEntity sendForgotPasswordEmail(@PathVariable final String email, final HttpServletRequest request) throws OperationException {
        final PasswordResetToken rToken = registrationService.createPasswordResetTokenForUser(email, request);
        final String json = registrationService.sendForgotPasswordEmail(rToken, request);

        return ResponseEntity.ok(json);
    }

    /**
     * Same functionality as:
     * @see RegistrationController#resendRegistrationToken(String, HttpServletRequest)
     */
    @ApiOperation(value = "Resend the forgot password token")
    @PostMapping(value = "/resendPasswordToken/{token}")
    public ResponseEntity resendForgotPasswordToken(@PathVariable("token") final String existingToken, HttpServletRequest request) throws OperationException {
        final PasswordResetToken rToken = registrationService.generateNewResetPasswordToken(existingToken, request);
        final String json = registrationService.sendForgotPasswordEmail(rToken, request);

        return ResponseEntity.ok(json);
    }

    /**
     * JavaScript sends this data after user clicks on the forgot password's link
     *
     * @param userId of user that forgot password
     * @param token variable in the path sent to email
     * @return 201 status if token is valid
     * @throws OperationException when token is invalid or expired
     */
    @ApiOperation(value = "Redirect user to \"Provide new password\" page when response is ACCEPTED after clicking the link in the email")
    @GetMapping(value = "/changePassword/{id}/{token}")
    public ResponseEntity validatePasswordResetToken(@PathVariable("id") final Long userId, @PathVariable("token") final String token) throws OperationException {
        registrationService.validatePasswordResetToken(userId, token);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new CustomResponseMessage<>(HttpStatus.ACCEPTED, ApiMessages.TOKEN_IS_VALID));
    }

    /**
     * After clicking the link in the email user is redirected to new page where he must enter new password
     * @param passwordCreate new password for user
     * @return 200 status with message if is ok
     * @throws OperationException when user is not allowed to change password
     */
    @ApiOperation(value = "Option available on \"Provide new password\" page. User enters a new password")
    @PostMapping(value = "/resetPassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity resetPassword(@RequestBody @Valid final PasswordCreate passwordCreate) throws OperationException {
            registrationService.resetUserPassword(passwordCreate);

        return ResponseEntity.ok().body(new CustomResponseMessage<>(HttpStatus.OK, ApiMessages.P_UPDATED_SUCCESSFULLY));
    }

    /**
     * Option available for authenticated users only
     * @param passwordUpdate old password and new password sent from the form
     */
    @ApiOperation(value = "Option available for logged in user who just want update a password")
    @PostMapping(value = "/updatePassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity changeUserPassword(@RequestBody @Valid final PasswordUpdate passwordUpdate) throws OperationException {
        // retrieve authenticated user's email
        final String email = authenticationFacade.getAuthentication().getName();

        registrationService.changeUserPassword(passwordUpdate, email);
        return ResponseEntity.ok(new CustomResponseMessage<>(HttpStatus.ACCEPTED, ApiMessages.P_UPDATED_SUCCESSFULLY));
    }

    private UserDTO convertToDTO(final User user) {
        return User.convertTo(user);
    }

    private User convertToEntity(final UserDTO user) {
        return User.convertFrom(user);
    }
}
