package pl.sokn.service.implementation;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.sokn.definitions.SoknDefinitions.ErrorMessages;
import pl.sokn.definitions.SoknDefinitions.Roles;
import pl.sokn.dto.PasswordCreate;
import pl.sokn.dto.PasswordUpdate;
import pl.sokn.entity.*;
import pl.sokn.exception.OperationException;
import pl.sokn.repository.PasswordResetTokenRepository;
import pl.sokn.repository.TokenRepository;
import pl.sokn.repository.UserRepository;
import pl.sokn.service.AuthorityService;
import pl.sokn.service.FieldOfArticleService;
import pl.sokn.service.RegistrationService;
import pl.sokn.service.helper.PasswordGenerator;
import pl.sokn.service.helper.SendEmailService;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@pl.sokn.annotation.qualifier.RegistrationService
public class RegistrationServiceImpl extends UserServiceImpl implements RegistrationService {
    public RegistrationServiceImpl(UserRepository userRepository,
                                   AuthorityService authorityService,
                                   FieldOfArticleService fieldOfArticleService,
                                   PasswordEncoder passwordEncoder,
                                   TokenRepository tokenRepository,
                                   PasswordResetTokenRepository passwordResetTokenRepository,
                                   SendEmailService sendEmailService) {
        super(userRepository, authorityService, fieldOfArticleService, passwordEncoder, tokenRepository, passwordResetTokenRepository, sendEmailService);
    }

    @Override
    public User save(final User user) throws OperationException {
        checkIfUserExists(user);

        final Authority defaultRole[] = new Authority[]{authorityService.retrieve(Roles.DEFAULT_ROLE)};

        return saveUser(user, defaultRole, false);
    }

    private void checkIfUserExists(final User user) throws OperationException {
        if (isUserInDB(user)) {
            throw new OperationException(HttpStatus.BAD_REQUEST, ErrorMessages.USER_ALREADY_EXISTS);
        }
    }

    private User saveUser(final User entity, final Authority roles[], final boolean enabled) {
        // new users that are only authors are disabled
        entity.setEnabled(enabled);
        // encode password
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));

        // add role for user
        // in this case spring inserts user and role's id into user_authorities table
        for (Authority role: roles)
            entity.getAuthorities().add(role);

        // save user in database and return entity with id
        return userRepository.save(entity);
    }

    @Override
    public void saveReviewer(User user) throws OperationException {
        checkIfUserExists(user);

        String password = PasswordGenerator.get(10);
        final Authority roles[] = new Authority[]{authorityService.retrieve(Roles.DEFAULT_ROLE),  authorityService.retrieve(Roles.REVIEWER_ROLE)};

        user.setPassword(password);
        // save user with roles author and reviewer, set user as enabled
        saveUser(user, roles, true);

        // if saved successfully then send an email with password
        emailService.constructReviewerRegistrationEmail(user.getEmail(), password);
    }

    @Override
    public void enableRegisteredUser(final String token) throws OperationException {
        final VerificationToken vToken = tokenRepository.findByToken(token);
        if (vToken == null)
            throw new OperationException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_TOKEN);

        final Calendar cal = Calendar.getInstance();
        if ((vToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0)
            throw new OperationException(HttpStatus.NOT_ACCEPTABLE, ErrorMessages.TOKEN_EXPIRED);

        final User user = vToken.getUser();
        // enable user if token is valid
        user.setEnabled(true);

        // we use save method also for UPDATING entities in the database
        userRepository.save(user);
    }

    @Override
    public VerificationToken createVerificationToken(final User user, final HttpServletRequest request) {
        final String token = UUID.randomUUID().toString();

        final VerificationToken myToken = new VerificationToken(token, user);
        return tokenRepository.save(myToken);
    }

    @Override
    public String sendRegistrationEmail(final VerificationToken vToken, final HttpServletRequest request) throws OperationException {
        final String token = vToken.getToken();
        final User user = vToken.getUser();
        emailService.constructRegistrationEmail(getAppUrl(request), token, user);

        return jsonService.constructRegistrationResponse(token, user.getEmail());
    }

    @Override
    public VerificationToken generateNewVerificationToken(String existingVerificationToken, HttpServletRequest request) throws OperationException {
        VerificationToken vToken = tokenRepository.findByToken(existingVerificationToken);
        if (vToken != null)
            vToken.updateToken(UUID.randomUUID().toString());

        if (vToken == null)
            throw new OperationException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_TOKEN);

        return tokenRepository.save(vToken);
    }

    @Override
    public PasswordResetToken createPasswordResetTokenForUser(String email, HttpServletRequest request) throws OperationException {

        final User user = Optional.ofNullable(retrieveByEmail(email))
                .orElseThrow(() -> new OperationException(HttpStatus.BAD_REQUEST, ErrorMessages.USER_DOES_NOT_EXISTS));

        final String token = UUID.randomUUID().toString();
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        user.setEnabled(false);
        userRepository.save(user);
        return passwordResetTokenRepository.save(myToken);
    }

    @Override
    public String sendForgotPasswordEmail(final PasswordResetToken rToken, final HttpServletRequest request) throws OperationException {
        final String token = rToken.getToken();
        final User user = rToken.getUser();

        emailService.constructForgotPasswordTokenEmail(getAppUrl(request), token, user);

        return jsonService.constructResetTokenResponse(token, user.getEmail());
    }

    @Override
    public PasswordResetToken generateNewResetPasswordToken(String currentToken, HttpServletRequest request) throws OperationException {
        final PasswordResetToken rToken = passwordResetTokenRepository.findByToken(currentToken);
        if (rToken != null)
            rToken.updateToken(UUID.randomUUID().toString());

        if (rToken == null)
            throw new OperationException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_TOKEN);

        return passwordResetTokenRepository.save(rToken);
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
