package pl.sokn.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import pl.sokn.entity.FieldOfArticle;
import pl.sokn.repository.ArticleRepository;
import pl.sokn.repository.FieldOfArticleRepository;
import pl.sokn.service.implementation.FieldOfArticleServiceImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class FieldOfArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private FieldOfArticleRepository fieldOfArticleRepository;

    @InjectMocks
    private FieldOfArticleServiceImpl fieldOfArticleServiceImpl;

    @Test
    public void checkAutowired() {
        assertNotNull(fieldOfArticleRepository);
        assertNotNull(articleRepository);
    }

    @Test
    public void getByField() {
        FieldOfArticle fieldOfArticle = new FieldOfArticle();
        fieldOfArticle.setField("Sztuczna Inteligencja");

        when(fieldOfArticleRepository.findByField("Sztuczna Inteligencja")).thenReturn(fieldOfArticle);

        FieldOfArticle realFieldOfArticle = fieldOfArticleServiceImpl.retrieveByField("Sztuczna Inteligencja");
        assertEquals(realFieldOfArticle.getField(), fieldOfArticle.getField());

        verify(fieldOfArticleRepository).findByField("Sztuczna Inteligencja");
    }
}
