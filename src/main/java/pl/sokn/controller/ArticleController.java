package pl.sokn.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.sokn.exception.OperationException;
import pl.sokn.service.ArticleService;

@Api(tags = "Article")
@RestController
@RequestMapping(path="/article")
public class ArticleController {
   private ArticleService articleService;

   @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping(path="/uploadArticle")
    public ResponseEntity uploadArticle(@RequestParam("file") MultipartFile file,
                                           @RequestParam("subject") String subjectId,
                                           @RequestParam("fieldOfArticle") String fieldOfArticle) throws OperationException {
       articleService.uploadArticle(file,subjectId,fieldOfArticle);
       return ResponseEntity.ok(HttpStatus.OK);
    }
}
