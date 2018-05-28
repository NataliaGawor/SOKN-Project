package pl.sokn.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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


    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @ManyToOne(targetEntity = FieldOfArticle.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false,name="field_id")
    private FieldOfArticle fieldOfArticle;

    @OneToOne(targetEntity = ArticleGrade.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "article_grade")
    private ArticleGrade articleGrade;

    public Article(String subject, String pathFile, User user, FieldOfArticle fieldOfArticle, ArticleGrade articleGrade) {
        this.subject = subject;
        this.pathFile = pathFile;
        this.user = user;
        this.fieldOfArticle = fieldOfArticle;
        this.articleGrade = articleGrade;
    }
}
