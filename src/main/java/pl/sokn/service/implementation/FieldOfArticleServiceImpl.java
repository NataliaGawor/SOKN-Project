package pl.sokn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sokn.entity.FieldOfArticle;
import pl.sokn.repository.FieldOfArticleRepository;
import pl.sokn.service.FieldOfArticleService;

@Service
public class FieldOfArticleServiceImpl extends AbstractGenericService<FieldOfArticle,Long> implements FieldOfArticleService {

    @Autowired
    FieldOfArticleServiceImpl(FieldOfArticleRepository fieldOfArticleRepository) {
        super(fieldOfArticleRepository);
    }
}
