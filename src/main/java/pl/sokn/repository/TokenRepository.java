package pl.sokn.repository;

import pl.sokn.entity.VerificationToken;

public interface TokenRepository extends GenericRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
    void deleteByUser_Id(Long id);
}
