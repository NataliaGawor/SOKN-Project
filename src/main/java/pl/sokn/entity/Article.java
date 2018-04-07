package pl.sokn.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import pl.sokn.dto.AuthorityDTO;
import pl.sokn.dto.UserDTO;
import pl.sokn.enums.Gender;

import javax.persistence.*;
import java.util.Optional;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

/**
 * Class with @Entity annotation will be generated in the database as a table
 */
@Entity
@Builder
@Data
@AllArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_article")
    private Long id;

    private String subject;
    private String path_file;
    private int grade_status;


    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public Article(String subject, String path_file, int grade_status) {
        this.subject = subject;
        this.path_file=path_file;
        this.grade_status=grade_status;
    }


}
