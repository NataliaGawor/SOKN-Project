package pl.sokn.service;

import pl.sokn.entity.User;

public interface UserService extends GenericService<User, Long> {
    User retrieveByCredentials(String credentials);
}
