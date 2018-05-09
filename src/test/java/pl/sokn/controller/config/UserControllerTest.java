package pl.sokn.controller.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import pl.sokn.definitions.SoknDefinitions;

import javax.json.JsonObject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest extends BaseTest {

    @Test
    public void checkIfRegistered() throws Exception {
        
        String response=
                mvc.perform(post(SoknDefinitions.Api.CHECK_IF_REGISTERED)
                        .content(write("admin@email.com"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                        .andReturn().getResponse().getContentAsString();

        JsonObject jsonObject = read(response);
    }

}
