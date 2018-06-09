package pl.sokn.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.sokn.definitions.SoknDefinitions.Roles;
import pl.sokn.entity.Authority;
import pl.sokn.entity.FieldOfArticle;
import pl.sokn.entity.User;
import pl.sokn.enums.Gender;
import pl.sokn.exception.OperationException;
import pl.sokn.repository.PasswordResetTokenRepository;
import pl.sokn.repository.TokenRepository;
import pl.sokn.repository.UserRepository;
import pl.sokn.service.helper.SendEmailService;
import pl.sokn.service.implementation.UserServiceImpl;

import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private static final Authority AUTHORITY = Authority.builder().role("USER").build();
    private static User user;
    
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthorityService authorityService;
    @Mock
    private FieldOfArticleService fieldOfArticleService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Mock
    private SendEmailService sendEmailService;

    @InjectMocks
    private UserServiceImpl userService;

    @Before
    public void beforeAll() {
        user = User.builder()
                .id(1L)
                .firstName("Test")
                .lastName("User")
                .email("junit@email.com")
                .enabled(true)
                .password("pass")
                .degree("Mgr")
                .gender(Gender.MALE)
                .affiliation("Singiel")
                .city("KRK")
                .country("PL")
                .authorities(newHashSet(AUTHORITY))
                .fieldOfArticles(newHashSet())
                .build();
    }
    
    @Test
    public void retrieveByEmail() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        final User retrieved = userService.retrieveByEmail(user.getEmail());
        
        assertNotNull(retrieved);
        assertEquals(user.getEmail(), retrieved.getEmail());
        assertEquals(user.getPassword(), retrieved.getPassword());
        assertEquals(user.getEmail(), retrieved.getEmail());
        assertTrue(retrieved.getAuthorities().contains(AUTHORITY));
    }
    @Test
    public void expectNullOnRetrieveUnregisteredUser() {
        final User retrieved = userService.retrieveByEmail("");
        assertNull(retrieved);
    }

    @Test
    public void deleteUser() throws OperationException {
        doNothing().when(userRepository).delete(user);
        userService.remove(user);
    }

    @Test
    public void shouldAddReviewerAuthority() throws OperationException {
        final Authority reviewer = Authority.builder().role(Roles.REVIEWER_ROLE).build();
        final String fieldObligatory = "Field";
        final String fieldAdditional = "Additional";

        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(authorityService.retrieve(reviewer.getRole())).thenReturn(reviewer);
        final FieldOfArticle field = FieldOfArticle.builder().field("Field").build();
        when(fieldOfArticleService.retrieveByField(fieldObligatory)).thenReturn(field);
        when(fieldOfArticleService.retrieveByField(fieldAdditional)).thenReturn(field);

        userService.addReviewerAuthority(user.getEmail(), fieldObligatory, fieldAdditional);

        assertTrue(user.getAuthorities().stream().anyMatch(authority -> authority.getRole().equals(Roles.REVIEWER_ROLE)));
        assertTrue(user.getFieldOfArticles().stream().anyMatch(f -> f.equals(field)));
    }

}