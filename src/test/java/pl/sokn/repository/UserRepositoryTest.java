package pl.sokn.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import pl.sokn.entity.User;
import pl.sokn.enums.Gender;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private Gender gender;
    @Before
    public void createUser() {
        entityManager.persist(new User("Prelegent", "Nazwisko", gender.MALE, "mgr",
                "prelegent@email.com", "pass", "Singiel", "Krk", "30-022",
                "Polska", true,null,null));
        entityManager.persist(new User("Prelegent", "Nazwisko", gender.MALE, "mgr",
                "author@email.com", "pass", "Singiel", "Krk", "30-022",
                "Polska", true,null,null));
    }

    @Test
    public void findByEmailOk(){
        User user=userRepository.findByEmail("prelegent@email.com");
        assertEquals(user.getEmail(),"prelegent@email.com");
        user=userRepository.findByEmail("author@email.com");
        assertEquals(user.getEmail(),"author@email.com");
    }

    @Test
    public void findByEmailNull(){
        User user=userRepository.findByEmail("nt@email.com");
        assertNull(user);
    }
}
