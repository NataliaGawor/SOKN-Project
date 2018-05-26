package pl.sokn.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pl.sokn.definitions.SoknDefinitions.Roles;
import pl.sokn.entity.Article;
import pl.sokn.entity.Authority;
import pl.sokn.entity.FieldOfArticle;
import pl.sokn.entity.User;
import pl.sokn.enums.Gender;
import pl.sokn.exception.OperationException;
import pl.sokn.service.ArticleService;
import pl.sokn.service.AuthorityService;
import pl.sokn.service.FieldOfArticleService;
import pl.sokn.service.UserService;

import javax.transaction.Transactional;
import java.io.File;
import java.util.Set;

/**
 * We save sample data in the database here
 */
@Component
public class DataLoader implements ApplicationRunner {

    private static final String SEPARATOR = File.separator;
    private static final String PASSWORD = "{bcrypt}$2a$10$usFd.2lfQzOVG/N45uDr7emsFOenWpAtwjqmROMqevyqou/eG26rS";
    private static final String KRAKOW = "Kraków";
    private static final String POLAND = "Poland";

    private final AuthorityService authorityService;
    private final UserService userService;
    private final FieldOfArticleService fieldOfArticleService;
    private final ArticleService articleService;

    @Autowired
    public DataLoader(AuthorityService authorityService,
                      @pl.sokn.annotation.qualifier.UserService UserService userService,
                      FieldOfArticleService fieldOfArticleService, ArticleService articleService) {
        this.authorityService = authorityService;
        this.userService = userService;
        this.fieldOfArticleService=fieldOfArticleService;
        this.articleService = articleService;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws OperationException {
        createUsers();
    }

    private void createUsers() throws OperationException {

        final Authority authorRole = Authority.builder().role(Roles.DEFAULT_ROLE).build();
        authorityService.save(authorRole);
        final Authority reviewerRole = Authority.builder().role(Roles.REVIEWER_ROLE).build();
        authorityService.save(reviewerRole);
        final Authority adminRole = Authority.builder().role(Roles.ADMIN_ROLE).build();
        authorityService.save(adminRole);
        final Authority passChangeRole = Authority.builder().role(Roles.PASS_CHANGE_ROLE).build();
        authorityService.save(passChangeRole);

        final FieldOfArticle fieldOne=FieldOfArticle.builder().field("Systemy Wbudowane").build();
        fieldOfArticleService.save(fieldOne);
        final FieldOfArticle fieldTwo=FieldOfArticle.builder().field("Sztuczna Inteligencja").build();
        fieldOfArticleService.save(fieldTwo);

        final User user = User.builder()
                .firstName("Author")
                .lastName("User")
                .email("prelegent@email.com")
                .enabled(true)
                .password(PASSWORD)
                .degree("Mgr")
                .gender(Gender.MALE)
                .affiliation("Singiel")
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
                .degree("Doktor")
                .gender(Gender.MALE)
                .affiliation("Singiel")
                .city(KRAKOW)
                .country(POLAND)
                .authorities(Set.of(adminRole))
                .build();

        userService.save(admin);

        final User reviewer = User.builder()
                .firstName("Reviewer")
                .lastName("User")
                .email("recenzent@email.com")
                .enabled(true)
                .password(PASSWORD)
                .degree("Doctor")
                .gender(Gender.MALE)
                .affiliation("Married")
                .city(KRAKOW)
                .country(POLAND)
                .authorities(Set.of(reviewerRole, authorRole))
                .fieldOfArticles(Set.of(fieldOne, fieldTwo))
                .build();

        userService.save(reviewer);


        final Article article = Article.builder()
                .gradeStatus(0)
                .pathFile("uploadFiles" + SEPARATOR + user.getId() + "_LPSUM.txt")
                .subject("LPSUM")
                .fieldOfArticle(fieldTwo)
                .user(user)
                .build();

        articleService.save(article);
    }
}
