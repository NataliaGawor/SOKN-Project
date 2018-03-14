package pl.sokn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sokn.entity.User;
import pl.sokn.repository.UserRepository;
import pl.sokn.service.UserService;


@Service
public class UserServiceImpl extends AbstractGenericService<User, Long> implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    @Override
    public User retrieveByCredentials(final String credentials) {
        return useCredentials(credentials);
    }

    private User useCredentials(final String credentials) {
        User userEntity = userRepository.findByUsername(credentials);
        if (userEntity == null)
            userEntity = userRepository.findByEmail(credentials);

        return userEntity;
    }
}
