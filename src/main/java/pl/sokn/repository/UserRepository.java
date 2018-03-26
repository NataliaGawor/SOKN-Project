package pl.sokn.repository;

import pl.sokn.entity.User;

public interface UserRepository extends GenericRepository<User, Long> {
    User findByEmail(String email);
    void deleteByEmail(String email);
}
