package pl.sokn.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import pl.sokn.entity.FieldOfArticle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@DataJpaTest
public class FieldOfArticleRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FieldOfArticleRepository fieldOfArticleRepository;

    private FieldOfArticle fieldOfArticle;

    @Before
    public void createFieldOfArticle(){
        entityManager.persist(new FieldOfArticle("Sztuczna Inteligencja"));
        entityManager.persist(new FieldOfArticle("Systemy Wbudowane"));
    }

    @Test
    public void findByFieldOK(){
        fieldOfArticle=fieldOfArticleRepository.findByField("Sztuczna Inteligencja");
        assertEquals("Sztuczna Inteligencja",fieldOfArticle.getField());
    }

    @Test
    public void findByFieldFalse(){
        fieldOfArticle=fieldOfArticleRepository.findByField("SW");
        assertFalse("SW".equals(fieldOfArticle.getField()));
    }
}
