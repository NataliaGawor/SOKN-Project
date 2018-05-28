package pl.sokn.service;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;
import pl.sokn.entity.Article;
import pl.sokn.repository.*;

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
        articleRepository=Mockito.mock(ArticleRepository.class);
        articleService=Mockito.mock(ArticleService.class);

    }
    public void getAllAuthorArticle(){
        articleService.getAllAuthorArticle("prelegent@email.com");
    }

}
