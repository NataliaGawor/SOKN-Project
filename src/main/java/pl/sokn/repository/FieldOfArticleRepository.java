package pl.sokn.repository;

import pl.sokn.entity.FieldOfArticle;

public interface FieldOfArticleRepository extends GenericRepository<FieldOfArticle, Long> {
    FieldOfArticle findByField(String field);
}
