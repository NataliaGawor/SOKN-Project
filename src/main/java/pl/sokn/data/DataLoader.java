package pl.sokn.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pl.sokn.entity.Authority;
import pl.sokn.entity.User;
import pl.sokn.enums.Gender;
import pl.sokn.exception.OperationException;
import pl.sokn.service.AuthorityService;
import pl.sokn.service.UserService;

import javax.transaction.Transactional;
import java.util.Set;

/**
 * We save sample data in the database here
 */
@Component
public class DataLoader implements ApplicationRunner {

    private static final String PASSWORD = "{bcrypt}$2a$10$usFd.2lfQzOVG/N45uDr7emsFOenWpAtwjqmROMqevyqou/eG26rS";
    private static final String KRAKOW = "Kraków";
    private static final String POLAND = "Poland";

    private final AuthorityService authorityService;
    private final UserService userService;

    @Autowired
    public DataLoader(AuthorityService authorityService,
                      @pl.sokn.annotation.qualifier.UserService UserService userService) {
        this.authorityService = authorityService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws OperationException {

        createUsers();

    }

    private void createUsers() throws OperationException {

        final Authority authorRole = Authority.builder().role("AUTHOR").build();
        authorityService.save(authorRole);
        final Authority reviewerRole = Authority.builder().role("REVIEWER").build();
        authorityService.save(reviewerRole);
        final Authority adminRole = Authority.builder().role("ADMIN").build();
        authorityService.save(adminRole);
        final Authority passChangeRole = Authority.builder().role("PASS_CHANGE").build();
        authorityService.save(passChangeRole);

        final User user = User.builder()
                .firstName("First")
                .lastName("User")
                .email("author@email.com")
                .enabled(true)
                .password(PASSWORD)
                .degree("Mgr")
                .gender(Gender.MALE)
                .affiliation("Single")
                .city(KRAKOW)
                .country(POLAND)
                .authorities(Set.of(authorRole))
                .build();

        userService.save(user);

        final User admin = User.builder()
                .firstName("Admin")
                .lastName("User")
                .email("admin@email.com")
                .enabled(true)
                .password(PASSWORD)
                .degree("Doctor")
                .gender(Gender.MALE)
                .affiliation("Single")
                .city(KRAKOW)
                .country(POLAND)
                .authorities(Set.of(adminRole))
                .build();

        userService.save(admin);

        final User reviewer = User.builder()
                .firstName("Reviewer")
                .lastName("User")
                .email("reviewer@email.com")
                .enabled(true)
                .password(PASSWORD)
                .degree("Doctor")
                .gender(Gender.MALE)
                .affiliation("zonaty")
                .city(KRAKOW)
                .country(POLAND)
                .authorities(Set.of(reviewerRole, authorRole))
                .build();

        userService.save(reviewer);
    }
}
