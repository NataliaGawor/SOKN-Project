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
import pl.sokn.service.ArticleGradeService;
import pl.sokn.service.ArticleService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends AbstractGenericService<Article, Long> implements ArticleService {
    // Windows and Unix systems use different conventions for the file separator!
    private static final String SEPARATOR = File.separator;

    private ArticleRepository articleRepository;
    private UserRepository userRepository;
    private FieldOfArticleRepository fieldOfArticleRepository;
    private ArticleGradeService articleGradeService;
    private static final String UPLOADED_FOLDER = "uploadFiles" + SEPARATOR;

    @Autowired
    public ArticleServiceImpl(GenericRepository<Article, Long> repository, ArticleRepository articleRepository,
                              UserRepository userRepository, FieldOfArticleRepository fieldOfArticleRepository,
                              ArticleGradeService articleGradeService) {
        super(repository);
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.fieldOfArticleRepository = fieldOfArticleRepository;
        this.articleGradeService = articleGradeService;
    }

    private void checkFile(MultipartFile file) throws OperationException {
        if (file.isEmpty())
            throw new OperationException(HttpStatus.NOT_ACCEPTABLE, "Pusty plik");

        final String fileName = file.getOriginalFilename();
        final String fileFormat = fileName.substring(fileName.lastIndexOf(".") + 1).trim();

        if (!fileFormat.equals("txt") && !fileFormat.equals("pdf") && !fileFormat.equals("docx"))
            throw new OperationException(HttpStatus.NOT_ACCEPTABLE, "Plik musi być w formacie pdf, docx lub txt");

        if (!fileName.substring(0, fileName.lastIndexOf(".")).matches("[a-zA-Z0-9]*"))
            throw new OperationException(HttpStatus.NOT_ACCEPTABLE, "Nazwa pliku może zawierać znaki alfanumeryczne(brak spacji).");
    }

    private String getPathToFile() {
        File fileToGetLocation = new File("");
        return fileToGetLocation.getAbsolutePath();
    }

    @Override
    public void updateArticleGrade(String email, Long articleId, int partGrade, String comment) throws OperationException {
        Article article = articleRepository.getOne(articleId);
        User user = userRepository.findByEmail(email);
        articleGradeService.addPartGrade(user.getId(), article.getArticleGrade(), partGrade, comment);
        //remove user email form this article
        //TO DO
    }

    @Override
    public void uploadArticle(String email, MultipartFile file, String subject, String fieldOfArticle) throws
            OperationException, IOException {
        checkArticle(subject);
        checkFile(file); //check if file is empty
        final User user = userRepository.findByEmail(email);
        saveFile(file, user.getId()); //save file
        FieldOfArticle field = fieldOfArticleRepository.getOne(Long.valueOf(fieldOfArticle));
        final ArticleGrade articleGrade = new ArticleGrade(0, 0, 0, "");
        articleGradeService.save(articleGrade);
        save(new Article(subject, UPLOADED_FOLDER + user.getId() + "_" + file.getOriginalFilename(), user, field, articleGrade));
    }


    @Override
    public void deleteArticle(Long articleId) throws OperationException {
        Article article = articleRepository.getOne(articleId);
        String url = getPathToFile() + SEPARATOR + article.getPathFile();

        try {
            Files.delete(Paths.get(url));
        } catch (IOException e) {
            throw new OperationException(HttpStatus.NOT_ACCEPTABLE, "Brak pliku do usunięcia");
        }
        removeById(articleId);
    }

    @Override
    public byte[] retrieve(String path) throws IOException {
        return Files.readAllBytes(Paths.get(UPLOADED_FOLDER + path));
    }

    @Override
    public List<Article> getAllAuthorArticle(String email) {
        return articleRepository.findByUser(userRepository.findByEmail(email));
    }

    @Override
    public List<Article> getArticlesToReview(String email) {
        final User user = userRepository.findByEmail(email);

        return articleRepository.findAll().stream()
                .filter(article -> user.getFieldOfArticles().contains(article.getFieldOfArticle()))
                .collect(Collectors.toList());
    }

    private void saveFile(MultipartFile file, Long id) throws IOException {
        final String fileName = file.getOriginalFilename();
        Files.write(Paths.get(UPLOADED_FOLDER + id + "_" + fileName), file.getBytes());
    }

    private void checkArticle(String article) throws OperationException {
        final Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        final Matcher m = p.matcher(article);
        if (m.find())
            throw new OperationException(HttpStatus.NOT_ACCEPTABLE, "Nazwa artykułu nie może zawierać znaków specjalnych");

        if (article.length() > 50)
            throw new OperationException(HttpStatus.NOT_ACCEPTABLE, "Nazwa artykułu może zawierać maksymalnie 50 znaków.");

        if (articleRepository.findBySubject(article) != null)
            throw new OperationException(HttpStatus.NOT_ACCEPTABLE, "Artykuł o podanej nazwie został już dodany.");
    }
}


