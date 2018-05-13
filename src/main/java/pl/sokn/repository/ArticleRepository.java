package pl.sokn.repository;

import pl.sokn.entity.Article;
import pl.sokn.entity.User;

import java.util.List;

public interface ArticleRepository extends GenericRepository<Article, Long> {
    Article findBySubject(String subject);
    List<Article> findByUser(User user);
}
