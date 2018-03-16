package pl.sokn.repository;

import pl.sokn.entity.PasswordResetToken;

public interface PasswordResetTokenRepository extends GenericRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
    void deleteByUser_Id(Long id);
}
