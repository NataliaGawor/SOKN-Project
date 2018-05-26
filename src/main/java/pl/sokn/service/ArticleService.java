package pl.sokn.service;

import org.springframework.web.multipart.MultipartFile;
import pl.sokn.entity.Article;
import pl.sokn.exception.OperationException;

import java.io.IOException;
import java.util.List;

public interface ArticleService extends GenericService<Article,Long>{

    List<Article> getAll(final String email);
    byte[] retrieve(final String path) throws IOException;
    void uploadArticle(String email,MultipartFile file,String subject,String fieldOfArticle) throws OperationException, IOException;
}
