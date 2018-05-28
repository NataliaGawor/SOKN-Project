package pl.sokn.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import pl.sokn.dto.AuthorityDTO;
import pl.sokn.dto.FieldOfArticleDTO;
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
public class User {

    /**
     * @apiNote @Id this field is primary key
     * @apiNote @GeneratedValue#GenerationType.IDENTITY database takes care about generating new id
     * @apiNote @Column custom name for column
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;

    private String firstName;
    private String lastName;
    // we store enum as a varchar in the database
    @Enumerated(value = EnumType.STRING)
    private Gender gender;
    private String degree;
    private String email;
    private String password;
    private String affiliation;
    private String city;
    private String zipCode;
    private String country;
    private Boolean enabled;

    public User(String firstName, String lastName, Gender gender, String degree,
                String email, String password, String affiliation, String city, String zipCode,
                String country, Boolean enabled, Set<Authority> authorities, Set<FieldOfArticle> fieldOfArticles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.degree = degree;
        this.email = email;
        this.password = password;
        this.affiliation = affiliation;
        this.city = city;
        this.zipCode = zipCode;
        this.country = country;
        this.enabled = enabled;
        this.authorities = authorities;
        this.fieldOfArticles = fieldOfArticles;
    }

    // settings for many-to-many relationship between user and authorities
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    // we set custom names for table and columns between user and authorities
    @JoinTable(name = "user_authorities",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id_user")},
            inverseJoinColumns = {@JoinColumn(name = "authority_id", referencedColumnName = "id_authority")})
    private Set<Authority> authorities;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_field",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id_user")},
            inverseJoinColumns = {@JoinColumn(name = "field_id", referencedColumnName = "id_field")})
    private Set<FieldOfArticle> fieldOfArticles;

    public User() {
        this.enabled = false;
        this.authorities = newHashSet();
        this.fieldOfArticles = newHashSet();
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
        user.setAffiliation(dto.getAffiliation());
        user.setCity(dto.getCity());
        user.setZipCode(dto.getZipCode());
        user.setCountry(dto.getCountry());
        user.setEnabled(dto.getEnabled());
        final Set<AuthorityDTO> roles = Optional.ofNullable(dto.getAuthorities()).orElse(Set.of());
        roles.forEach(i -> user.getAuthorities().add(Authority.convertFrom(i)));
        final Set<FieldOfArticleDTO> fields = Optional.ofNullable(dto.getFieldOfArticles()).orElse(Set.of());
        fields.forEach(i -> user.getFieldOfArticles().add(FieldOfArticle.convertFrom(i)));

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
        user.setAffiliation(entity.getAffiliation());
        user.setCity(entity.getCity());
        user.setZipCode(entity.getZipCode());
        user.setCountry(entity.getCountry());
        user.setEnabled(entity.getEnabled());
        final Set<Authority> roles = Optional.ofNullable(entity.getAuthorities()).orElse(Set.of());
        roles.forEach(i -> user.getAuthorities().add(AuthorityDTO.builder().id(i.getId()).role(i.getRole()).build()));
        final Set<FieldOfArticle> fields = Optional.ofNullable(entity.getFieldOfArticles()).orElse(Set.of());
        fields.forEach(i -> user.getFieldOfArticles().add(FieldOfArticleDTO.builder().id(i.getId()).field(i.getField()).build()));

        return user;
    }
}
