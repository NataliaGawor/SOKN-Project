package pl.sokn.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
