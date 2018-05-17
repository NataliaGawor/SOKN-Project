package pl.sokn.service;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import pl.sokn.dto.EmailMessage;
import pl.sokn.entity.User;
import pl.sokn.exception.OperationException;

public interface UserService extends GenericService<User, Long> {
    User retrieveByEmail(String email);
    User isUserInDB(final String email, final HttpServletRequest request)throws OperationException;
    void sendEmail(final EmailMessage emailDTO, final HttpServletRequest request)throws OperationException;
    void addReviewerAuthority(final String email, final String fieldObligatory, final String fieldAdditional) throws OperationException;
}
