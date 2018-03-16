package pl.sokn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.sokn.entity.User;
import pl.sokn.repository.PasswordResetTokenRepository;
import pl.sokn.repository.TokenRepository;
import pl.sokn.repository.UserRepository;
import pl.sokn.service.AuthorityService;
import pl.sokn.service.UserService;


@Service
@pl.sokn.annotation.qualifier.UserService
public class UserServiceImpl extends AbstractGenericService<User, Long> implements UserService {

    final UserRepository userRepository;
    final AuthorityService authorityService;
    final PasswordEncoder passwordEncoder;
    final TokenRepository tokenRepository;
    final PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           AuthorityService authorityService,
                           PasswordEncoder passwordEncoder,
                           TokenRepository tokenRepository,
                           PasswordResetTokenRepository passwordResetTokenRepository) {
        super(userRepository);
        this.userRepository = userRepository;
        this.authorityService = authorityService;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @Override
    public User retrieveByCredentials(final String credentials) {
        User userEntity = userRepository.findByUsername(credentials);
        if (userEntity == null)
            userEntity = userRepository.findByEmail(credentials);

        return userEntity;
    }

    @Override
    public User retrieveByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User retrieveByName(final String name) {
        return userRepository.findByUsername(name);
    }

    boolean isUserInDB(final User user) {
        return retrieveByEmail(user.getEmail()) != null || retrieveByName(user.getUsername()) != null;
    }
}
