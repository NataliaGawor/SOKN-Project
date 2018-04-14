package pl.sokn.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

import static com.google.common.collect.Sets.newHashSet;

/**
 * Class with @Entity annotation will be generated in the database as a table
 */
@Entity
@Builder
@Data
@AllArgsConstructor
public class ReviewersGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reviewer_grade")
    private Long id;

    private String numberOfArticles;
    private int positive;
    private int negative;
    private int neutral;


    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public ReviewersGrade(String numberOfArticles, int positive, int negative, int neutral) {
        this.numberOfArticles = numberOfArticles;
        this.positive=positive;
        this.negative=negative;
        this.neutral=neutral;
    }


}
