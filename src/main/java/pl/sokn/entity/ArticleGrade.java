package pl.sokn.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_article_grade")
    private Long id;

    private int positive;
    private int negative;
    private int neutral;
    private String comment;

    public ArticleGrade(int positive, int negative, int neutral, String comment) {
        this.positive = positive;
        this.negative = negative;
        this.neutral = neutral;
        this.comment = comment;
    }
}
