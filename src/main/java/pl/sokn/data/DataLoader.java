package pl.sokn.data;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Component
public class DataLoader implements ApplicationRunner {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        entityManager.createNativeQuery(
                "INSERT IGNORE INTO `authority` (`id_authority`, `description`, `role`) VALUES\n" +
                        "  (1, 'Default role for user', 'USER'),\n" +
                        "  (2, 'Admin - Has permission to perform admin tasks', 'ADMIN');"
        ).executeUpdate();

        entityManager.createNativeQuery(
                "INSERT IGNORE INTO `user` (`id_user`, `email`, `enabled`, `password`, `username`) VALUES\n" +
                        "  (1, 'user@email.com', 1, '{bcrypt}$2a$10$usFd.2lfQzOVG/N45uDr7emsFOenWpAtwjqmROMqevyqou/eG26rS', 'user'),\n" +
                        "  (2, 'admin@email.com', 1, '{bcrypt}$2a$10$usFd.2lfQzOVG/N45uDr7emsFOenWpAtwjqmROMqevyqou/eG26rS', 'admin');"
        ).executeUpdate();

        entityManager.createNativeQuery(
                "INSERT IGNORE INTO `user_authorities` (`user_id`, `authority_id`) VALUES\n" +
                        "  (1, 1),\n" +
                        "  (2, 1),\n" +
                        "  (2, 2);"
        ).executeUpdate();
    }
}
