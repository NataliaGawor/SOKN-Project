package pl.sokn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sokn.entity.Article;
import pl.sokn.repository.ArticleRepository;
import pl.sokn.repository.GenericRepository;
import pl.sokn.service.ArticleService;

@Service
public class ArticleServiceImpl extends AbstractGenericService<Article,Long> implements ArticleService{
    private ArticleRepository articleRepository;

    @Autowired
    public ArticleServiceImpl(GenericRepository<Article, Long> repository, ArticleRepository articleRepository) {
        super(repository);
        this.articleRepository = articleRepository;
    }

}
