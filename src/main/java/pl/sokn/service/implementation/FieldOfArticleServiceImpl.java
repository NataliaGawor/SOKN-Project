package pl.sokn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sokn.entity.FieldOfArticle;
import pl.sokn.repository.ArticleRepository;
import pl.sokn.repository.GenericRepository;
import pl.sokn.service.FieldOfArticleService;

@Service
public class FieldOfArticleServiceImpl extends AbstractGenericService<FieldOfArticle,Long> implements FieldOfArticleService {
    private ArticleRepository articleRepository;

    @Autowired
    public FieldOfArticleServiceImpl(GenericRepository<FieldOfArticle, Long> repository, ArticleRepository articleRepository) {
        super(repository);
        this.articleRepository = articleRepository;
    }
}
