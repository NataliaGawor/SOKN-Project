package pl.sokn.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.sokn.dto.FieldOfArticleDTO;

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

    public static FieldOfArticle convertFrom(final FieldOfArticleDTO dto) {
        if (dto == null)
            return null;

        final FieldOfArticle roleImpl = new FieldOfArticle();
        roleImpl.setId(dto.getId());
        roleImpl.setField(dto.getField());

        return roleImpl;
    }

    public static FieldOfArticleDTO convertTo(final FieldOfArticle entity) {
        if (entity == null)
            return null;

        final FieldOfArticleDTO roleImpl = new FieldOfArticleDTO();
        roleImpl.setId(entity.getId());
        roleImpl.setField(entity.getField());

        return roleImpl;
    }
}
