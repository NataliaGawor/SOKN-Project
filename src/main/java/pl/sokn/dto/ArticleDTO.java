package pl.sokn.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.sokn.entity.ArticleGrade;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {

    private Long id;
    private String subject;
    private String pathFile;
    private ArticleGrade articleGrade;
    private UserDTO user;
    private FieldOfArticleDTO fieldOfArticle;
}
