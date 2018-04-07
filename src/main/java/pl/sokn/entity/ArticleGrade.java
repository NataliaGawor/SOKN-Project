package pl.sokn.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

/**
 * The entity needed for storing registration token in the database
 */
@Entity
@Data
@NoArgsConstructor
public class ArticleGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_article_grade")
    private Long id;

    private int positive;
    private int negative;
    private int neutral;

    @OneToOne(targetEntity = Article.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "article_id")
    private Article article;

    public ArticleGrade(int positive, int negative, int neutral) {
        this.positive=positive;
        this.negative = negative;
        this.neutral = neutral;
    }
}
