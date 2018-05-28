package pl.sokn.service;

import org.springframework.web.multipart.MultipartFile;
import pl.sokn.entity.Article;
import pl.sokn.exception.OperationException;

import java.io.IOException;
import java.util.List;

public interface ArticleService extends GenericService<Article,Long>{
    void uploadArticle(String email,MultipartFile file,String subject,String fieldOfArticle) throws OperationException;
    void saveArticle(MultipartFile file,Long id) throws OperationException;
    void deleteArticle(Long articleId) throws OperationException;
    byte[] retrieve(final String path) throws IOException;
    List<Article> getAllAuthorArticle(String email);
    List<Article> getAll(final String email);

}
