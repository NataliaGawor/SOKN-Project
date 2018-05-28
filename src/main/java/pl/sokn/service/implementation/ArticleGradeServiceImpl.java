package pl.sokn.service.implementation;

import org.springframework.stereotype.Service;
import pl.sokn.entity.ArticleGrade;
import pl.sokn.repository.GenericRepository;
import pl.sokn.service.ArticleGradeService;

@Service
public class ArticleGradeServiceImpl extends AbstractGenericService<ArticleGrade,Long> implements ArticleGradeService{


    ArticleGradeServiceImpl(GenericRepository<ArticleGrade, Long> repository) {
        super(repository);
    }
}
