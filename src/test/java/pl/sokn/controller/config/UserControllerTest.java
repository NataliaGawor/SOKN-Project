package pl.sokn.controller.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest extends BaseTest {
    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void checkIfRegistered() throws Exception {

//        RequestBuilder request= MockMvcRequestBuilders
//                .post(SoknDefinitions.Api.CHECK_IF_REGISTERED)
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content("admin@email.com");
//
//        String result = mvc.perform(post(SoknDefinitions.Api.CHECK_IF_REGISTERED)
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(write( new String("admin@email.com"))))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andReturn().getResponse().getContentAsString();

//          String result=mvc.perform(request)
//                  .andDo(print())
//                  .andExpect(status().isOk())
//                  .andReturn().getResponse().getContentAsString();

//
//        assertEquals(HttpStatus.OK,response.getStatus());
    }

}
