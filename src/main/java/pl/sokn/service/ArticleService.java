package pl.sokn.service;

import org.springframework.web.multipart.MultipartFile;
import pl.sokn.entity.Article;
import pl.sokn.exception.OperationException;

public interface ArticleService extends GenericService<Article,Long>{
    void checkFile(MultipartFile file) throws OperationException;
    void uploadArticle(MultipartFile file,String subject,String fieldOfArticle) throws OperationException;
    void saveFile(MultipartFile file) throws OperationException;
}
