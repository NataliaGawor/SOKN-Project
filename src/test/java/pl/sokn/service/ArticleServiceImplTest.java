package pl.sokn.service;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;
import pl.sokn.entity.Article;
import pl.sokn.exception.OperationException;
import pl.sokn.repository.*;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class ArticleServiceImplTest {

    private GenericRepository<Article, Long> repository;
    private ArticleService articleService;
    private ArticleRepository articleRepository;
    private UserRepository userRepository;
    private FieldOfArticleRepository fieldOfArticleRepository;
    private ArticleGradeRepository articleGradeRepository;

    @Before
    public void setUp() {
        articleRepository= Mockito.mock(ArticleRepository.class);
        articleService=Mockito.mock(ArticleService.class);

    }
    @Test
    public void getAllAuthorArticle(){
       List<Article> articles= articleService.getAllAuthorArticle("prelegent@email.com");
       assertNotNull(articles);
    }

    @Test
    public void removeArticleTest() throws OperationException {
        articleService.deleteArticle(Long.valueOf( 1));
        Article article=articleRepository.getOne(Long.valueOf(1));
        assertNull(article);
    }

    @Test
    public void getArticleToReview(){
       List<Article> articles= articleService.getArticlesToReview("recenzent@email.com");
       assertTrue(articles.size()==0);
    }
}
