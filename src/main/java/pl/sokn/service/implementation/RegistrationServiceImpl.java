package pl.sokn.service.implementation;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.sokn.definitions.SoknDefinitions.ErrorMessages;
import pl.sokn.definitions.SoknDefinitions.Roles;
import pl.sokn.dto.PasswordCreate;
import pl.sokn.dto.PasswordUpdate;
import pl.sokn.entity.Authority;
import pl.sokn.entity.User;
import pl.sokn.entity.VerificationToken;
import pl.sokn.exception.OperationException;
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
                                   TokenRepository tokenRepository) {
        super(userRepository, authorityService, passwordEncoder, tokenRepository);
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
            throw new OperationException(HttpStatus.CONFLICT, ErrorMessages.INVALID_TOKEN);

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
            throw new OperationException(HttpStatus.CONFLICT, ErrorMessages.INVALID_TOKEN);

        final VerificationToken newToken = tokenRepository.save(vToken);
        final User tokenUser = newToken.getUser();
        emailService.constructRegistrationEmail(getAppUrl(request), newToken.getToken(), tokenUser);

        return jsonService.constructRegistrationResponse(newToken.getToken(), tokenUser.getEmail());
    }

    @Override
    public String createPasswordResetTokenForUser(String email, HttpServletRequest request) throws OperationException {
        return null;
    }

    @Override
    public String generateNewResetPasswordToken(String currentToken, HttpServletRequest request) throws OperationException {
        return null;
    }

    @Override
    public void validatePasswordResetToken(Long id, String token) throws OperationException {

    }

    @Override
    public void changeUserPassword(PasswordUpdate password, String credentials) throws OperationException {

    }

    @Override
    public void resetUserPassword(PasswordCreate passwordCreate) throws OperationException {

    }
}
