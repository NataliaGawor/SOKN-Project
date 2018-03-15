package pl.sokn.repository;

import org.springframework.stereotype.Repository;
import pl.sokn.entity.Authority;

@Repository
public interface AuthorityRepository extends GenericRepository<Authority, Long> {
    Authority findByRole(String role);
}
