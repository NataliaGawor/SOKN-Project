package pl.sokn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.sokn.entity.Article;
import pl.sokn.entity.ArticleGrade;
import pl.sokn.entity.FieldOfArticle;
import pl.sokn.entity.User;
import pl.sokn.exception.OperationException;
import pl.sokn.repository.*;
import pl.sokn.service.ArticleService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ArticleServiceImpl extends AbstractGenericService<Article,Long> implements ArticleService{
    private ArticleRepository articleRepository;
    private UserRepository userRepository;
    private FieldOfArticleRepository fieldOfArticleRepository;
    private ArticleGradeRepository articleGradeRepository;
    private static String UPLOADED_FOLDER="\\uploadFiles\\";

    @Autowired
    public ArticleServiceImpl(GenericRepository<Article, Long> repository, ArticleRepository articleRepository,
                                UserRepository userRepository,FieldOfArticleRepository fieldOfArticleRepository,
                                ArticleGradeRepository articleGradeRepository) {
        super(repository);
        this.articleRepository = articleRepository;
        this.userRepository=userRepository;
        this.fieldOfArticleRepository=fieldOfArticleRepository;
        this.articleGradeRepository=articleGradeRepository;
    }

    @Override
    public void checkFile(MultipartFile file) throws OperationException {
        if(file.isEmpty())
            throw new OperationException(HttpStatus.NOT_ACCEPTABLE,"Pusty plik");

        String fileName=file.getOriginalFilename();
        String fileFormat=fileName.substring(fileName.lastIndexOf(".") + 1).trim();

        if(!fileFormat.equals("txt") && !fileFormat.equals("pdf") && !fileFormat.equals("docx"))
            throw new OperationException(HttpStatus.NOT_ACCEPTABLE,"Plik musi być w formacie pdf,docx lub txt");

        if(!fileName.substring(0,fileName.lastIndexOf(".")).matches("[a-zA-Z0-9]*"))
            throw new OperationException(HttpStatus.NOT_ACCEPTABLE,"Nazwa pliku może zawierać wyłącznie litery i liczby.");
    }

    @Override
    public void uploadArticle(String email,MultipartFile file, String subject, String fieldOfArticle) throws OperationException {
        checkArticle(subject);
        checkFile(file); //check if file is empty
        User user=userRepository.findByEmail(email);
        saveFile(file,user.getId()); //save file
        FieldOfArticle field=fieldOfArticleRepository.getOne(Long.valueOf(fieldOfArticle));
        ArticleGrade articleGrade=new ArticleGrade(0,0,0,"");
        articleGradeRepository.save(articleGrade);
        save(new Article(subject,UPLOADED_FOLDER+user.getId()+"_"+file.getOriginalFilename(),user,field,articleGrade));
    }

    @Override
    public void saveFile(MultipartFile file,Long id) throws OperationException {
        String fileName = file.getOriginalFilename();

        byte[] bytes;
        try{
            bytes = file.getBytes();
            BufferedOutputStream buffStream =
                    new BufferedOutputStream(new FileOutputStream(new File(getPathTofile() +"\\uploadFiles\\"+id+"_"+fileName)));
            buffStream.write(bytes);
            buffStream.close();
        } catch (IOException e) {
            throw new OperationException(HttpStatus.NOT_ACCEPTABLE,"Nie udało się dodać artykułu");
        }
    }

    @Override
    public void checkArticle(String article) throws OperationException {
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(article);
        if (m.find())
            throw new OperationException(HttpStatus.NOT_ACCEPTABLE,"Nazwa artykułu nie może zawierać znaków specjalnych");

        if(article.length()>50)
            throw new OperationException(HttpStatus.NOT_ACCEPTABLE,"Nazwa artykułu może zawierać maksymalnie 50 znaków.");

        if(articleRepository.findBySubject(article)!=null)
            throw new OperationException(HttpStatus.NOT_ACCEPTABLE,"Artykuł o podanej nazwie został już dodany.");
    }

    @Override
    public void deleteArticle( Long articleId) throws OperationException {
        Article article=articleRepository.getOne(articleId);
        String url=getPathTofile()+article.getPathFile();

        try {
            Files.delete(Paths.get(url));
        } catch (IOException e) {
            throw new OperationException(HttpStatus.NOT_ACCEPTABLE,"Brak pliku do usunięcia");
        }
        removeById(articleId);
    }

    @Override
    public List<Article> getAllAuthorArticle(String email){
        return articleRepository.findByUser(userRepository.findByEmail(email));
    }

    private String getPathTofile(){
        File fileToGetLocation = new File("");
        return fileToGetLocation.getAbsolutePath();
    }

}

