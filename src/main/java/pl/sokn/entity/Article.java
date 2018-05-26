package pl.sokn.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.sokn.dto.ArticleDTO;
import pl.sokn.dto.FieldOfArticleDTO;
import pl.sokn.dto.UserDTO;

import javax.persistence.*;

/**
 * Class with @Entity annotation will be generated in the database as a table
 */
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_article")
    private Long id;

    private String subject;
    private String pathFile;
    private int gradeStatus;


    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @ManyToOne(targetEntity = FieldOfArticle.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "field_id")
    private FieldOfArticle fieldOfArticle;

    public Article(String subject, String pathFile, int gradeStatus, User user, FieldOfArticle fieldOfArticle) {
        this.subject = subject;
        this.pathFile = pathFile;
        this.gradeStatus = gradeStatus;
        this.user = user;
        this.fieldOfArticle = fieldOfArticle;
    }

    public static ArticleDTO convertTo(final Article article) {
        if (article == null)
            return null;

        return ArticleDTO.builder()
                .id(article.getId())
                .subject(article.getSubject())
                .pathFile(article.getPathFile())
                .gradeStatus(article.getGradeStatus())
                .user(UserDTO.builder().id(article.getUser().getId()).build())
                .fieldOfArticle(FieldOfArticleDTO.builder()
                        .id(article.getFieldOfArticle().getId())
                        .field(article.getFieldOfArticle().getField())
                        .build())
                .build();
    }
}
