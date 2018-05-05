package pl.sokn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sokn.service.FieldOfArticleService;

@Api(tags = "Fields Of Articles")
@RestController
@RequestMapping(path = "/fieldsOfArticles")
public class FieldOfArticleController{
    private FieldOfArticleService fieldOfArticleService;

    @Autowired
    public FieldOfArticleController(FieldOfArticleService fieldOfArticleService) {
        this.fieldOfArticleService = fieldOfArticleService;
    }

    @ApiOperation(value = "Get all article fields")
    @GetMapping(path="/getAll")
    public ResponseEntity getAllFields(){
        return ResponseEntity.ok(fieldOfArticleService.getAll());
    }
}
