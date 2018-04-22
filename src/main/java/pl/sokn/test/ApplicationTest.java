package pl.sokn.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.sokn.controller.RegistrationController;

import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value=RegistrationController.class)
public class ApplicationTest {
    @Autowired
    private MockMvc mvc;

//    @Test
//    public void shouldReturnUser() throws Exception {
//        this.mvc.perform(post("/logIn")
//                        .param("username","user@email.com")
//                        .param("password","pass")
//                        .andExpect(status().isOk())
//                        ;
//    }
}
