package pl.sokn.service;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import pl.sokn.entity.User;
import pl.sokn.exception.OperationException;

public interface UserService extends GenericService<User, Long> {
    User retrieveByEmail(String email);
    User isUserInDB(final String email, final HttpServletRequest request)throws OperationException;
}
