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
import pl.sokn.security.information.AuthenticationFacade;
import pl.sokn.service.ArticleService;

@Api(tags = "Article")
@RestController
@RequestMapping(path="/article")
public class ArticleController {
   private ArticleService articleService;
   private AuthenticationFacade authenticationFacade;

   @Autowired
    public ArticleController(ArticleService articleService,AuthenticationFacade authenticationFacade) {
        this.articleService = articleService;
        this.authenticationFacade=authenticationFacade;
    }

    @PostMapping(path="/uploadArticle")
    public ResponseEntity uploadArticle(@RequestParam("file") MultipartFile file,
                                           @RequestParam("subject") String subjectId,
                                           @RequestParam("fieldOfArticle") String fieldOfArticle) throws OperationException {
       String email = authenticationFacade.getAuthentication().getName();
       articleService.uploadArticle(email,file,subjectId,fieldOfArticle);
       return ResponseEntity.ok(HttpStatus.OK);
    }
}
