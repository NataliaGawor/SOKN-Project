package pl.sokn.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.sokn.entity.User;
import pl.sokn.repository.UserRepository;

import java.util.stream.Collectors;

@Service
@Primary
public class CustomUserDetailService implements UserDetailsService {

    private static final String USER_NOT_FOUND = "User not found";
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String credentials) {
        User user = userRepository.findByUsername(credentials);
        if (user == null)
            user = userRepository.findByEmail(credentials);

        if (user == null)
            throw new UsernameNotFoundException(USER_NOT_FOUND);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getEnabled(),
                true,
                true,
                true,
                user.getAuthorities()
                        .stream()
                        .map(authority -> new SimpleGrantedAuthority(authority.getRole()))
                        .collect(Collectors.toSet()));
    }
}
