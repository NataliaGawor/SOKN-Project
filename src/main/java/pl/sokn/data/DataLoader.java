package pl.sokn.data;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 * We save sample data in the database here
 */
@Component
public class DataLoader implements ApplicationRunner {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        entityManager.createNativeQuery(
                "INSERT IGNORE INTO authority (id_authority, description, role) VALUES\n" +
                        "  (1, 'Default role for user', 'AUTHOR'),\n" +
                        "  (2, 'Admin - Has permission to perform admin tasks', 'ADMIN'),\n" +
                        "  (3, 'Password Change - Role for user who clicked \"forgot password\"', 'PASS_CHANGE');"
        ).executeUpdate();

        entityManager.createNativeQuery(
                "INSERT IGNORE INTO\n" +
                        "  user (id_user, first_name, last_name, email, enabled, password, degree, gender)\n" +
                        "VALUES\n" +
                        "  (1, 'First', 'User', 'user@email.com', 1, '{bcrypt}$2a$10$usFd.2lfQzOVG/N45uDr7emsFOenWpAtwjqmROMqevyqou/eG26rS', 'MGR', 'MALE'),\n" +
                        "  (2, 'Admin', 'User', 'admin@email.com', 1, '{bcrypt}$2a$10$usFd.2lfQzOVG/N45uDr7emsFOenWpAtwjqmROMqevyqou/eG26rS', 'DOCTOR', 'MALE');"
        ).executeUpdate();

        entityManager.createNativeQuery(
                "INSERT IGNORE INTO user_authorities (user_id, authority_id) VALUES\n" +
                        "  (1, 1),\n" +
                        "  (2, 1),\n" +
                        "  (2, 2);"
        ).executeUpdate();
    }
}
