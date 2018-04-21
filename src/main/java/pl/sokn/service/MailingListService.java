package pl.sokn.service;

import pl.sokn.entity.MailingList;

public interface MailingListService extends GenericService<MailingList, String>{
    boolean checkIfSignedIn(final String email);
}
