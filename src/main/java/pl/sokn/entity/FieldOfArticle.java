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
public class FieldOfArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_field")
    private Long id;

    private String field;

    // settings for many-to-many relationship between user and authorities
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    // we set custom names for table and columns between user and authorities
    @JoinTable(name = "user_field",
            joinColumns = {@JoinColumn(name = "field_id", referencedColumnName = "id_field")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id_user")})
    private Set<User> user;

    public FieldOfArticle(String field) {
        this.field = field;
    }


}
