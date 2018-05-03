package pl.sokn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.sokn.entity.Article;
import pl.sokn.entity.FieldOfArticle;
import pl.sokn.entity.User;
import pl.sokn.exception.OperationException;
import pl.sokn.repository.ArticleRepository;
import pl.sokn.repository.FieldOfArticleRepository;
import pl.sokn.repository.GenericRepository;
import pl.sokn.repository.UserRepository;
import pl.sokn.service.ArticleService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class ArticleServiceImpl extends AbstractGenericService<Article,Long> implements ArticleService{
    private ArticleRepository articleRepository;
    private UserRepository userRepository;
    private FieldOfArticleRepository fieldOfArticleRepository;
    private static String UPLOADED_FOLDER="C:/xampp/htdocs/files/";


    @Autowired
    public ArticleServiceImpl(GenericRepository<Article, Long> repository, ArticleRepository articleRepository,
                                UserRepository userRepository,FieldOfArticleRepository fieldOfArticleRepository) {
        super(repository);
        this.articleRepository = articleRepository;
        this.userRepository=userRepository;
        this.fieldOfArticleRepository=fieldOfArticleRepository;
    }

    @Override
    public void checkFile(MultipartFile file) throws OperationException {
        if(file.isEmpty())
            throw new OperationException(HttpStatus.NOT_ACCEPTABLE,"Pusty plik");
    }

    @Override
    public void uploadArticle(String email,MultipartFile file, String subject, String fieldOfArticle) throws OperationException {
        checkFile(file);
        saveFile(file);
        User user=userRepository.findByEmail(email);
        FieldOfArticle field=fieldOfArticleRepository.getOne(Long.valueOf(fieldOfArticle));
        save(new Article(subject,UPLOADED_FOLDER+file.getOriginalFilename(),0,user,field));
    }

    @Override
    public void saveFile(MultipartFile file) throws OperationException {
        String fileName = file.getOriginalFilename();
        byte[] bytes;
        try{
            bytes = file.getBytes();
            BufferedOutputStream buffStream =
                    new BufferedOutputStream(new FileOutputStream(new File(UPLOADED_FOLDER + fileName)));
            buffStream.write(bytes);
            buffStream.close();
        } catch (IOException e) {
            throw new OperationException(HttpStatus.NOT_ACCEPTABLE,"Nie udało się dodać artykułu");
        }
    }
}

