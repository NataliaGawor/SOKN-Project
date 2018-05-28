package pl.sokn.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {

    private Long id;
    private String subject;
    private String pathFile;
    private int gradeStatus;
    private UserDTO user;
    private FieldOfArticleDTO fieldOfArticle;
}
