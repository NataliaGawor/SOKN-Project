package pl.sokn.service;

import org.springframework.web.multipart.MultipartFile;
import pl.sokn.entity.Article;
import pl.sokn.exception.OperationException;

import java.util.List;

public interface ArticleService extends GenericService<Article,Long>{
    void checkFile(MultipartFile file) throws OperationException;
    void uploadArticle(String email,MultipartFile file,String subject,String fieldOfArticle) throws OperationException;
    void saveFile(MultipartFile file,Long id) throws OperationException;
    void checkArticle(String article) throws OperationException;
    List<Article> getAllAuthorArticle(String email);
}
