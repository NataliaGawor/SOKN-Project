package pl.sokn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sokn.entity.FieldOfArticle;
import pl.sokn.repository.ArticleRepository;
import pl.sokn.repository.FieldOfArticleRepository;
import pl.sokn.repository.GenericRepository;
import pl.sokn.service.FieldOfArticleService;

@Service
public class FieldOfArticleServiceImpl extends AbstractGenericService<FieldOfArticle,Long> implements FieldOfArticleService {
    private ArticleRepository articleRepository;
    final FieldOfArticleRepository fieldOfArticleRepository;

    @Autowired
    public FieldOfArticleServiceImpl(FieldOfArticleRepository fieldOfArticleRepository, ArticleRepository articleRepository) {
        super(fieldOfArticleRepository);
        this.fieldOfArticleRepository = fieldOfArticleRepository;
        this.articleRepository = articleRepository;
    }


    @Override
    public FieldOfArticle retrieveByField(String field) {
        return fieldOfArticleRepository.findByField(field);
    }
}
