package pl.sokn.repository;

import io.swagger.annotations.ApiOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import pl.sokn.entity.MailingList;

import java.sql.SQLException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MailingListRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MailingListRepository mailingListRepository;

    private MailingList mailingList;

    @Before
    public void addEmail(){
        mailingList = new MailingList("testwy@emial.com");
        entityManager.persist(mailingList);
    }

    @Test
    public void save(){
        mailingList = mailingListRepository.save(new MailingList("adres@email.com"));
        assertNotNull(mailingList);
        assertEquals("adres@email.com", mailingList.getEmail());
    }

    @Test
    public void retrieve(){
        MailingList returned = mailingListRepository.findById(mailingList.getEmail()).get();
        assertNotNull(mailingList);
        assertEquals(mailingList.getEmail(), returned.getEmail());
    }

    @Test
    public void retrieveFail(){
        mailingList = mailingListRepository.findById("nieIstenie@email.com").orElse(null);
        assertNull(mailingList);
    }

}
