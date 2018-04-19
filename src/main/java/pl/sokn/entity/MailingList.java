package pl.sokn.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.sokn.dto.MailingListDTO;

import javax.persistence.*;
import javax.validation.constraints.Email;

/**
 * The entity needed for storing email addresses added to newsletter
 */
@Entity
@Data
@NoArgsConstructor
public class MailingList {

    @Id
    @Email
    private String email;

    public MailingList( final String email) {
        super();
        this.email = email;
    }

    public static MailingList convertFrom(final MailingListDTO dto){
        if(dto == null)
            return null;

        final MailingList mailingList = new MailingList();
        mailingList.setEmail(dto.getEmail());

        return mailingList;
    }

    public static MailingListDTO convertTo(final MailingList entity){
        if(entity == null)
            return null;

        final MailingListDTO mailingList = new MailingListDTO();
        mailingList.setEmail(entity.getEmail());

        return mailingList;
    }
}
