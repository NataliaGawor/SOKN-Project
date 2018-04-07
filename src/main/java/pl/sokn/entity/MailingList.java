package pl.sokn.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * The entity needed for storing registration token in the database
 */
@Entity
@Data
@NoArgsConstructor
public class MailingList {

    @Id
    private String email;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;


    public MailingList( final User user) {
        super();
        this.email = email;
        this.user = user;
    }

}
