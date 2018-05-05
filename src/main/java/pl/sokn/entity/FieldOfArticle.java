package pl.sokn.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

/**
 * Class with @Entity annotation will be generated in the database as a table
 */
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldOfArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_field")
    private Long id;

    private String field;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_field",
            joinColumns = {@JoinColumn(name = "field_id", referencedColumnName = "id_field")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id_user")})
    private Set<User> user;

    public FieldOfArticle(String field) {
        this.field = field;
    }
}
