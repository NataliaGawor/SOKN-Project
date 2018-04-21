package pl.sokn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.sokn.definitions.SoknDefinitions.ErrorMessages;
import pl.sokn.entity.MailingList;
import pl.sokn.exception.OperationException;
import pl.sokn.repository.MailingListRepository;
import pl.sokn.service.MailingListService;

@Service
public class MailingListServiceImpl extends AbstractGenericService<MailingList, String>  implements MailingListService{

    private final MailingListRepository mailingListRepository;

    @Autowired
    public MailingListServiceImpl(MailingListRepository mailingListRepository) {
        super(mailingListRepository);
        this.mailingListRepository = mailingListRepository;
    }

    @Override
    public MailingList save(final MailingList mailingList) throws OperationException{
        if(checkIfSignedIn(mailingList.getEmail()))
            throw new OperationException(HttpStatus.BAD_REQUEST, ErrorMessages.EMAIL_ALREADY_SUBSCRIBED);

        return mailingListRepository.save(mailingList);
    }

    @Override
    public boolean checkIfSignedIn(final String email){
        if(retrieve(email)!= null)
            return true;
        return false;
    }

    @Override
    public void remove(final MailingList mailingList) throws OperationException{
        if(!checkIfSignedIn(mailingList.getEmail()))
            throw new OperationException(HttpStatus.BAD_REQUEST, ErrorMessages.CANNOT_UNSUBSCRIBE);

        mailingListRepository.delete(mailingList);

    }
}
