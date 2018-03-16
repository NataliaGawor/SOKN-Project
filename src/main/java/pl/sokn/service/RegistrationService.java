package pl.sokn.service;

import pl.sokn.dto.PasswordCreate;
import pl.sokn.dto.PasswordUpdate;
import pl.sokn.entity.User;
import pl.sokn.exception.OperationException;

import javax.servlet.http.HttpServletRequest;

public interface RegistrationService extends UserService {

    void enableRegisteredUser(final String token) throws OperationException;

    String createVerificationToken(final User user, final HttpServletRequest request);

    String generateNewVerificationToken(final String existingVerificationToken, HttpServletRequest request) throws OperationException;

    String createPasswordResetTokenForUser(String email, HttpServletRequest request);

    String generateNewResetPasswordToken(final String currentToken, HttpServletRequest request) throws OperationException;

    void validatePasswordResetToken(Long id, String token) throws OperationException;

    void resetUserPassword(final PasswordCreate passwordCreate) throws OperationException;

    void changeUserPassword(PasswordUpdate password, String credentials) throws OperationException;
}
