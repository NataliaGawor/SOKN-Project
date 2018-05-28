package pl.sokn.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pl.sokn.entity.*;
import pl.sokn.enums.Gender;
import pl.sokn.exception.OperationException;
import pl.sokn.service.*;

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
    private final FieldOfArticleService fieldOfArticleService;
    private final ArticleService articleService;
    private final ArticleGradeService articleGradeService;

    @Autowired
    public DataLoader(AuthorityService authorityService,
                      @pl.sokn.annotation.qualifier.UserService UserService userService,
                     FieldOfArticleService fieldOfArticleService,ArticleService articleService,
                        ArticleGradeService articleGradeService){
        this.authorityService = authorityService;
        this.userService = userService;
        this.fieldOfArticleService=fieldOfArticleService;
        this.articleService=articleService;
        this.articleGradeService=articleGradeService;
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

        final FieldOfArticle fieldOne=FieldOfArticle.builder().field("Systemy Wbudowane").build();
        fieldOfArticleService.save(fieldOne);
        final FieldOfArticle fieldTwo=FieldOfArticle.builder().field("Sztuczna Inteligencja").build();
        fieldOfArticleService.save(fieldTwo);

        final User user = User.builder()
                .firstName("First")
                .lastName("User")
                .email("author@email.com")
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
                .email("reviewer@email.com")
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



        fieldOfArticleService.save(fieldOne);
        fieldOfArticleService.save(fieldTwo);

        final ArticleGrade grade=ArticleGrade.builder()
                .negative(0)
                .neutral(0)
                .positive(0)
                .comment("nieźle")
                .build();

        final ArticleGrade grade1=ArticleGrade.builder()
                .negative(1)
                .neutral(1)
                .positive(1)
                .comment("super")
                .build();

        final ArticleGrade grade2=ArticleGrade.builder()
                .negative(1)
                .neutral(1)
                .positive(1)
                .comment("słaby")
                .build();

        final ArticleGrade grade3=ArticleGrade.builder()
                .negative(0)
                .neutral(1)
                .positive(2)
                .comment("lepiej")
                .build();

        articleGradeService.save(grade);
        articleGradeService.save(grade1);
        articleGradeService.save(grade2);
        articleGradeService.save(grade3);

        final Article articleOne=Article.builder()
                .subject("Dynamika")
                .pathFile("\\uploadFiles\\1_dyn.txt")
                .user(user)
                .fieldOfArticle(fieldOne)
                .articleGrade(grade)
                .build();

         articleService.save(articleOne);

        final Article articleTwo=Article.builder()
                .subject("Fizyka")
                .pathFile("\\uploadFiles\\2_fiz.txt")
                .user(admin)
                .fieldOfArticle(fieldOne)
                .articleGrade(grade1)
                .build();

        articleService.save(articleTwo);

        final Article articleThree=Article.builder()
                .subject("Matematyka")
                .pathFile("\\uploadFiles\\1_mat.txt")
                .user(user)
                .fieldOfArticle(fieldOne)
                .articleGrade(grade2)
                .build();

        articleService.save(articleThree);

        final Article articleFour=Article.builder()
                .subject("Biologia")
                .pathFile("\\uploadFiles\\1_biol.txt")
                .user(user)
                .fieldOfArticle(fieldOne)
                .articleGrade(grade3)
                .build();

        articleService.save(articleFour);

    }
}
