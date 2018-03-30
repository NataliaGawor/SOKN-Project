package pl.sokn.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.sokn.dto.AuthorityDTO;

import javax.persistence.*;

/**
 * The entity with roles, e.g. ADMIN
 */
@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_authority")
    private Long id;

    private String role;

    private String description;

    public static Authority convertFrom(final AuthorityDTO dto) {
        if (dto == null)
            return null;

        final Authority roleImpl = new Authority();
        roleImpl.setId(dto.getId());
        roleImpl.setRole(dto.getRole());
        roleImpl.setDescription(dto.getDescription());

        return roleImpl;
    }

    public static AuthorityDTO convertTo(final Authority entity) {
        if (entity == null)
            return null;

        final AuthorityDTO roleImpl = new AuthorityDTO();
        roleImpl.setId(entity.getId());
        roleImpl.setRole(entity.getRole());
        roleImpl.setDescription(entity.getDescription());

        return roleImpl;
    }
}
