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

@Entity
@Builder
@Data
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;

    private String firstName;
    private String lastName;
    @Enumerated(value = EnumType.STRING)
    private Gender gender;
    private String degree;
    private String email;
    private String password;
    private Boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_authorities",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id_user")},
            inverseJoinColumns = {@JoinColumn(name = "authority_id", referencedColumnName = "id_authority")})
    private Set<Authority> authorities;

    public User() {
        this.enabled = false;
        this.authorities = newHashSet();
    }

    public static User convertFrom(final UserDTO dto) {
        if (dto == null)
            return null;

        final User user = new User();
        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setGender(dto.getGender());
        user.setDegree(dto.getDegree());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setEnabled(dto.getEnabled());
        final Set<AuthorityDTO> roles = Optional.ofNullable(dto.getAuthorities()).orElse(Set.of());
        roles.forEach(i -> user.getAuthorities().add(Authority.convertFrom(i)));

        return user;
    }

    public static UserDTO convertTo(final User entity) {
        if (entity == null)
            return null;

        final UserDTO user = new UserDTO();
        user.setId(entity.getId());
        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());
        user.setGender(entity.getGender());
        user.setDegree(entity.getDegree());
        user.setEmail(entity.getEmail());
        user.setEnabled(entity.getEnabled());
        final Set<Authority> roles = Optional.ofNullable(entity.getAuthorities()).orElse(Set.of());
        roles.forEach(i -> user.getAuthorities().add(AuthorityDTO.builder().id(i.getId()).role(i.getRole()).build()));

        return user;
    }
}
