package pl.sokn.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.sokn.definitions.SoknDefinitions;
import pl.sokn.dto.ArticleDTO;
import pl.sokn.entity.Article;
import pl.sokn.exception.OperationException;
import pl.sokn.security.information.AuthenticationFacade;
import pl.sokn.service.ArticleService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@io.swagger.annotations.Api(tags = "Article")
@RestController
@RequestMapping(path = "")
public class ArticleController {
    private final ArticleService articleService;
    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public ArticleController(ArticleService articleService, AuthenticationFacade authenticationFacade) {
        this.articleService = articleService;
        this.authenticationFacade=authenticationFacade;
    }

    @ApiOperation(value = "add Article")
    @PostMapping(path="/uploadArticle")
    public ResponseEntity uploadArticle(@RequestParam("file") MultipartFile file,
                                        @RequestParam("subject") String subjectId,
                                        @RequestParam("fieldOfArticle") String fieldOfArticle) throws OperationException, IOException {


        final String email = authenticationFacade.getAuthentication().getName();
        articleService.uploadArticle(email, file, subjectId, fieldOfArticle);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping(path = SoknDefinitions.Api.REVIEWER_PATH + "/article", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ArticleDTO>> getArticles() {

        final String email = authenticationFacade.getAuthentication().getName();

        final List<Article> articles = articleService.getAll(email);

        return ResponseEntity.ok(articles.stream()
                .map(Article::convertTo)
                .collect(Collectors.toList()));
    }

    @GetMapping(path = SoknDefinitions.Api.REVIEWER_PATH + "/article/{path}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> getArticle(@PathVariable final String path) throws IOException {
        final byte[] bytes = articleService.retrieve(path);

        return ResponseEntity.status(200)
                .header(HttpHeaders.CONTENT_DISPOSITION, "filename=" + path)
                .body(bytes);
    }
    @ApiOperation(value = "Get all articles")
    @GetMapping(path="/getAllArticles",produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllArticle(){
       String email = authenticationFacade.getAuthentication().getName();
       return ResponseEntity.ok(articleService.getAllAuthorArticle(email));
    }

    @ApiOperation(value = "Remove article")
    @PostMapping(path="/removeArticle",consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity removeArticle(@RequestBody String id) throws OperationException {
        articleService.deleteArticle(Long.parseLong(id));
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
    @ApiOperation(value = "Get article")
    @PostMapping(path="/getArticle",consumes= MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity getArticleOne(@RequestBody String id){
       return  ResponseEntity.ok(articleService.retrieve(Long.parseLong(id)));
    }
}
