package pl.sokn.service.implementation;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.sokn.definitions.SoknDefinitions.ErrorMessages;
import pl.sokn.definitions.SoknDefinitions.Roles;
import pl.sokn.dto.PasswordCreate;
import pl.sokn.dto.PasswordUpdate;
import pl.sokn.entity.Authority;
import pl.sokn.entity.PasswordResetToken;
import pl.sokn.entity.User;
import pl.sokn.entity.VerificationToken;
import pl.sokn.exception.OperationException;
import pl.sokn.repository.PasswordResetTokenRepository;
import pl.sokn.repository.TokenRepository;
import pl.sokn.repository.UserRepository;
import pl.sokn.service.AuthorityService;
import pl.sokn.service.RegistrationService;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.UUID;

@Service
@pl.sokn.annotation.qualifier.RegistrationService
public class RegistrationServiceImpl extends UserServiceImpl implements RegistrationService {
    public RegistrationServiceImpl(UserRepository userRepository,
                                   AuthorityService authorityService,
                                   PasswordEncoder passwordEncoder,
                                   TokenRepository tokenRepository,
                                   PasswordResetTokenRepository passwordResetTokenRepository) {
        super(userRepository, authorityService, passwordEncoder, tokenRepository, passwordResetTokenRepository);
    }

    @Override
    public User save(final User user) throws OperationException {
        checkIfUserExists(user);

        final Authority defaultRole = authorityService.retrieve(Roles.DEFAULT_ROLE);

        return saveUser(user, defaultRole);
    }

    private void checkIfUserExists(final User user) throws OperationException {
        if (isUserInDB(user)) {
            throw new OperationException(HttpStatus.BAD_REQUEST, ErrorMessages.USER_ALREADY_EXISTS);
        }
    }

    private User saveUser(final User entity, final Authority role) {
        entity.setEnabled(false);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));

        entity.getAuthorities().add(role);

        return userRepository.save(entity);
    }

    @Override
    public void enableRegisteredUser(String token) throws OperationException {
        final VerificationToken vToken = tokenRepository.findByToken(token);
        if (vToken == null)
            throw new OperationException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_TOKEN);

        final Calendar cal = Calendar.getInstance();
        if ((vToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0)
            throw new OperationException(HttpStatus.NOT_ACCEPTABLE, ErrorMessages.TOKEN_EXPIRED);

        final User user = vToken.getUser();
        user.setEnabled(true);

        userRepository.save(user);
    }

    @Override
    public String createVerificationToken(User user, HttpServletRequest request) {
        final String token = UUID.randomUUID().toString();

        final VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);

        emailService.constructRegistrationEmail(getAppUrl(request), token, user);

        return jsonService.constructRegistrationResponse(token, user.getEmail());
    }

    @Override
    public String generateNewVerificationToken(String existingVerificationToken, HttpServletRequest request) throws OperationException {
        VerificationToken vToken = tokenRepository.findByToken(existingVerificationToken);
        if (vToken != null)
            vToken.updateToken(UUID.randomUUID().toString());

        if (vToken == null)
            throw new OperationException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_TOKEN);

        final VerificationToken newToken = tokenRepository.save(vToken);
        final User tokenUser = newToken.getUser();
        emailService.constructRegistrationEmail(getAppUrl(request), newToken.getToken(), tokenUser);

        return jsonService.constructRegistrationResponse(newToken.getToken(), tokenUser.getEmail());
    }

    @Override
    public String createPasswordResetTokenForUser(String email, HttpServletRequest request) {
        final User user = retrieveByEmail(email);
        //if exists
        final String token = UUID.randomUUID().toString();
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        user.setEnabled(false);
        userRepository.save(user);
        passwordResetTokenRepository.save(myToken);

        emailService.constructForgotPasswordTokenEmail(getAppUrl(request), token, user);

        return jsonService.constructResetTokenResponse(token, email);
    }

    @Override
    public String generateNewResetPasswordToken(String currentToken, HttpServletRequest request) throws OperationException {
        final PasswordResetToken rToken = passwordResetTokenRepository.findByToken(currentToken);
        if (rToken != null)
            rToken.updateToken(UUID.randomUUID().toString());

        if (rToken == null)
            throw new OperationException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_TOKEN);

        final PasswordResetToken saved = passwordResetTokenRepository.save(rToken);
        final User user = saved.getUser();

        emailService.constructForgotPasswordTokenEmail(getAppUrl(request), saved.getToken(), user);

        return jsonService.constructResetTokenResponse(rToken.getToken(), user.getEmail());
    }

    @Override
    public void validatePasswordResetToken(Long id, String token) throws OperationException {
        final PasswordResetToken passToken = passwordResetTokenRepository.findByToken(token);
        if ((passToken == null) || (!passToken.getUser().getId().equals(id)))
            throw new OperationException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_TOKEN);

        final Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0)
            throw new OperationException(HttpStatus.BAD_REQUEST, ErrorMessages.TOKEN_EXPIRED);

        final User user = passToken.getUser();
        user.setEnabled(true);

        final Authority authority = authorityService.retrieve(Roles.PASS_CHANGE_ROLE);
        user.getAuthorities().add(authority);

        userRepository.save(user);
    }

    @Override
    public void resetUserPassword(PasswordCreate passwordCreate) throws OperationException {
        final User user = userRepository.findById(passwordCreate.getUserId()).orElse(null);
        if (user == null)
            throw new OperationException(HttpStatus.BAD_REQUEST, ErrorMessages.USER_DOES_NOT_EXISTS);

        final boolean match = user.getAuthorities().stream()
                .anyMatch(authority -> Roles.PASS_CHANGE_ROLE.equalsIgnoreCase(authority.getRole()));

        if (!match)
            throw new OperationException(HttpStatus.FORBIDDEN, ErrorMessages.P_CHANGE_NOT_ALLOWED);

        if (validateOldPassword(passwordCreate.getPassword(), user.getPassword()))
            throw new OperationException(HttpStatus.BAD_REQUEST, ErrorMessages.P_ARE_THE_SAME);

        user.getAuthorities().remove(authorityService.retrieve(Roles.PASS_CHANGE_ROLE));
        user.setPassword(passwordEncoder.encode(passwordCreate.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void changeUserPassword(PasswordUpdate password, String credentials) throws OperationException {
        final User entity = retrieveByEmail(credentials);
        if (validateOldPassword(password.getPassword(), entity.getPassword()))
            throw new OperationException(HttpStatus.BAD_REQUEST, ErrorMessages.P_ARE_THE_SAME);

        // oldPassword property provided by User has to be the same as entity's password
        if (!validateOldPassword(password.getOldPassword(), entity.getPassword()))
            throw new OperationException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_OLD_P);

        entity.setPassword(passwordEncoder.encode(password.getPassword()));
        userRepository.save(entity);
    }

    private boolean validateOldPassword(final String oldPassword, final String oldUserPassword) {
        return passwordEncoder.matches(oldPassword, oldUserPassword);
    }
}
