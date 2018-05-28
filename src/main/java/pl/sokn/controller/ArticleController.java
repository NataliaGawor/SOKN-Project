package pl.sokn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    @ApiOperation(value = "add Article")
    @PostMapping(path="/uploadArticle")
    public ResponseEntity uploadArticle(@RequestParam("file") MultipartFile file,
                                           @RequestParam("subject") String subjectId,
                                           @RequestParam("fieldOfArticle") String fieldOfArticle) throws OperationException {
       String email = authenticationFacade.getAuthentication().getName();
       articleService.uploadArticle(email,file,subjectId,fieldOfArticle);
       return ResponseEntity.ok(HttpStatus.OK);
    }
    @ApiOperation(value = "Get all articles")
    @GetMapping(path="/getAllArticles",produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllArticle(){
       String email = authenticationFacade.getAuthentication().getName();
       return ResponseEntity.ok(articleService.getAllAuthorArticle(email));
    }

    @ApiOperation(value = "Remove article")
    @PostMapping(path="/removeArticle",consumes= MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity removeArticle(@RequestBody String id) throws OperationException {
        articleService.deleteArticle(Long.parseLong(id));
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
}
