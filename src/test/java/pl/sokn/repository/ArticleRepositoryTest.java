package pl.sokn.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import pl.sokn.entity.*;
import pl.sokn.enums.Gender;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ArticleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    private Gender gender;
    private Article article;

    @Before
    public void createArticle(){  //article to test
        Authority authority=entityManager.persist(new Authority("AUTHOR"));
        FieldOfArticle fieldOfArticle=entityManager.persist(new FieldOfArticle("Sztuczna Inteligencja"));
        User user=createUser(authority,fieldOfArticle,"prelegent@email.com");
        User user1=createUser(authority,fieldOfArticle,"administrator@email.com");
        ArticleGrade articleGrade = entityManager.persist(new ArticleGrade(0, 0, 0, ""));
        entityManager.persist(new Article("fiz","path",user,fieldOfArticle,articleGrade));
        entityManager.persist(new Article("mat","path",user,fieldOfArticle,articleGrade));
        entityManager.persist(new Article("fizmat","path",user1,fieldOfArticle,articleGrade));
    }

    private User createUser(Authority authority,FieldOfArticle fieldOfArticle,String email){
        return entityManager.persist(new User("Prelegent", "Nazwisko", gender.MALE, "mgr",
                email, "pass", "Singiel", "Krk", "30-022",
                "Polska", true,  Set.of(authority),Set.of(fieldOfArticle)));
    }
    @Test
    public void findBySubjectOK() {
        article = articleRepository.findBySubject("fiz");
        assertEquals("fiz", article.getSubject());
    }

    @Test
    public void findBySubjectFalse(){
        article = articleRepository.findBySubject("fiz");
        System.out.println(article.getSubject());
        assertFalse("fizmat".equals(article.getSubject()));
    }

    @Test
    public void findByUserOK(){
        User user=userRepository.findByEmail("prelegent@email.com");
        List<Article> atricles=articleRepository.findByUser(user);
        assertTrue(atricles.size()>0);
        assertTrue(atricles.get(0).getSubject().equals("fiz"));
        assertTrue(atricles.get(1).getSubject().equals("mat"));
    }
}
