package pl.sokn.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pl.sokn.definitions.SoknDefinitions.Roles;
import pl.sokn.entity.*;
import pl.sokn.enums.Gender;
import pl.sokn.exception.OperationException;
import pl.sokn.service.*;

import javax.transaction.Transactional;
import java.io.File;
import java.util.Set;

/**
 * We save sample data in the database here
 */
@Component
public class DataLoader implements ApplicationRunner {

    private static final String SEPARATOR = File.separator;
    private static final String UPLOADED_FOLDER = "uploadFiles" + SEPARATOR;
    private static final String PASSWORD = "{bcrypt}$2a$10$usFd.2lfQzOVG/N45uDr7emsFOenWpAtwjqmROMqevyqou/eG26rS";
    private static final String KRAKOW = "Kraków";
    private static final String POLAND = "Poland";

    private final AuthorityService authorityService;
    private final UserService userService;
    private final FieldOfArticleService fieldOfArticleService;
    private final ArticleService articleService;
    private final ArticleGradeService articleGradeService;


    @Autowired
    public DataLoader(AuthorityService authorityService,
                      @pl.sokn.annotation.qualifier.UserService UserService userService,

                      FieldOfArticleService fieldOfArticleService, ArticleService articleService,
                      ArticleGradeService articleGradeService) {
        this.authorityService = authorityService;
        this.userService = userService;
        this.fieldOfArticleService = fieldOfArticleService;
        this.articleService = articleService;
        this.articleGradeService = articleGradeService;
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

        final FieldOfArticle aiField = FieldOfArticle.builder().field("Sztuczna Inteligencja").build();
        fieldOfArticleService.save(aiField);
        final FieldOfArticle mathField = FieldOfArticle.builder().field("Matematyka").build();
        fieldOfArticleService.save(mathField);
        final FieldOfArticle embeddedField = FieldOfArticle.builder().field("Systemy Wbudowane").build();
        fieldOfArticleService.save(embeddedField);

        final User user = User.builder()
                .firstName("Prelegent")
                .lastName("Naziwsko")
                .email("prelegent@email.com")
                .enabled(true)
                .password(PASSWORD)
                .degree("Magister")
                .gender(Gender.MALE)
                .affiliation("Kawaler / Panna")
                .city(KRAKOW)
                .country(POLAND)
                .authorities(Set.of(authorRole))
                .build();

        userService.save(user);

        final User admin = User.builder()
                .firstName("Administrator")
                .lastName("SOKN")
                .email("admin@email.com")
                .enabled(true)
                .password(PASSWORD)
                .degree("Doktor")
                .gender(Gender.MALE)
                .affiliation("Kawaler / Panna")
                .city(KRAKOW)
                .country(POLAND)
                .authorities(Set.of(adminRole))
                .build();

        userService.save(admin);

        final User reviewer = User.builder()
                .firstName("Recenzent")
                .lastName("Nazwisko")
                .email("recenzent@email.com")
                .enabled(true)
                .password(PASSWORD)
                .degree("Doktor")
                .gender(Gender.MALE)
                .affiliation("Zonaty / Zamezna")
                .city(KRAKOW)
                .zipCode("32-456")
                .country(POLAND)
                .authorities(Set.of(reviewerRole, authorRole))
                .fieldOfArticles(Set.of(aiField, mathField, embeddedField))
                .build();

        userService.save(reviewer);

        final User reviewer2 = User.builder()
                .firstName("Recenzent2")
                .lastName("Nazwisko2")
                .email("recenzent2@email.com")
                .enabled(true)
                .password(PASSWORD)
                .degree("Magister")
                .gender(Gender.MALE)
                .affiliation("Kawaler / Panna")
                .city(KRAKOW)
                .zipCode("32-456")
                .country(POLAND)
                .authorities(Set.of(reviewerRole, authorRole))
                .fieldOfArticles(Set.of(embeddedField))
                .build();

        userService.save(reviewer2);

        final ArticleGrade grade = ArticleGrade.builder()
                .negative(0)
                .neutral(0)
                .positive(0)
                .comment("niezle")
                .build();

        final ArticleGrade grade1 = ArticleGrade.builder()
                .negative(1)
                .neutral(0)
                .positive(0)
                .reviewerIdOne(1L)
                .comment("super")
                .build();

        final ArticleGrade grade2 = ArticleGrade.builder()
                .negative(1)
                .neutral(0)
                .positive(1)
                .reviewerIdOne(1L)
                .reviewerIdTwo(2L)
                .comment("slaby")
                .build();

        final ArticleGrade grade3 = ArticleGrade.builder()
                .negative(0)
                .neutral(1)
                .positive(0)
                .reviewerIdOne(1L)
                .comment("lepiej")
                .build();

        articleGradeService.save(grade);
        articleGradeService.save(grade1);
        articleGradeService.save(grade2);
        articleGradeService.save(grade3);

        final Article mathArticle = Article.builder()
                .subject("Dane Bibliometryczne")
                .pathFile(UPLOADED_FOLDER + "1_DaneBibliometryczne.pdf")
                .user(user)
                .fieldOfArticle(mathField)
                .articleGrade(grade1)
                .build();

        articleService.save(mathArticle);

        final Article aiArticle = Article.builder()
                .subject("Rozwoj Szanse I Zagrozenia")
                .pathFile(UPLOADED_FOLDER + "1_Rozwoj_Szanse_I_Zagrozenia.pdf")
                .user(user)
                .fieldOfArticle(aiField)
                .articleGrade(grade2)
                .build();

        articleService.save(aiArticle);

        final Article embeddedArticle = Article.builder()
                .subject("Nauczanie Systemow Wbudowanych")
                .pathFile(UPLOADED_FOLDER + "1_Nauczanie_Systemow_Wbudowanych.pdf")
                .user(user)
                .fieldOfArticle(embeddedField)
                .articleGrade(grade3)
                .build();

        articleService.save(embeddedArticle);

        final Article embeddedArticle2 = Article.builder()
                .subject("Arduino")
                .pathFile(UPLOADED_FOLDER + "2_Arduino.pdf")
                .user(admin)
                .fieldOfArticle(embeddedField)
                .articleGrade(grade3)
                .build();

        articleService.save(embeddedArticle2);
    }
}
