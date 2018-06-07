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

        final FieldOfArticle fieldOne = FieldOfArticle.builder().field("Systemy Wbudowane").build();
        fieldOfArticleService.save(fieldOne);
        final FieldOfArticle fieldTwo = FieldOfArticle.builder().field("Sztuczna Inteligencja").build();
        fieldOfArticleService.save(fieldTwo);

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
                .fieldOfArticles(Set.of(fieldOne, fieldTwo))
                .build();

        userService.save(reviewer);

//        fieldOfArticleService.save(fieldOne);
//        fieldOfArticleService.save(fieldTwo);

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

        final Article articleOne = Article.builder()
                .subject("Dynamika")
                .pathFile(UPLOADED_FOLDER + "1_dyn.txt")
                .user(user)
                .fieldOfArticle(fieldOne)
                .articleGrade(grade)
                .build();

        articleService.save(articleOne);

        final Article articleTwo = Article.builder()
                .subject("Fizyka")
                .pathFile(UPLOADED_FOLDER + "2_fiz.txt")
                .user(admin)
                .fieldOfArticle(fieldOne)
                .articleGrade(grade1)
                .build();

        articleService.save(articleTwo);

        final Article articleThree = Article.builder()
                .subject("Matematyka")
                .pathFile(UPLOADED_FOLDER + "1_mat.txt")
                .user(user)
                .fieldOfArticle(fieldOne)
                .articleGrade(grade2)
                .build();

        articleService.save(articleThree);

//        final Article articleFour = Article.builder()
//                .subject("Biologia")
//                .pathFile(UPLOADED_FOLDER + "1_biol.txt")
//                .user(user)
//                .fieldOfArticle(fieldOne)
//                .articleGrade(grade3)
//                .build();
//
//        articleService.save(articleFour);

        final Article grapheneArticle = Article.builder()
                .subject("Grafen")
                .pathFile(UPLOADED_FOLDER + "1_GrafenMaterial.pdf")
                .user(user)
                .fieldOfArticle(fieldTwo)
                .articleGrade(grade3)
                .build();

        articleService.save(grapheneArticle);
    }
}
