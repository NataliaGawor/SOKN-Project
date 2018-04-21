package pl.sokn.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import pl.sokn.controller.config.BaseTest;
import pl.sokn.definitions.SoknDefinitions.AuthorizationHelper;
import pl.sokn.security.jwt.model.AuthenticationRequest;

import javax.json.JsonObject;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest extends BaseTest {

    @Test
    public void claimTokenAndRefresh() throws Exception {
        final AuthenticationRequest request = AuthenticationRequest.builder()
                .email("admin@email.com")
                .password("pass")
                .build();

        final String response =
                mvc.perform(post("/login")
                        .content(write(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                        .andReturn().getResponse().getContentAsString();

        final JsonObject jsonObject = read(response);
        assertNotNull(jsonObject);
        final JsonObject user = jsonObject.getJsonObject("user");
        assertNotNull(user);
        final String accessToken = jsonObject.getString("token");
        assertThat(accessToken, startsWith("ey"));
        assertThat(user.getString("email"), is(request.getEmail()));

        mvc.perform(get("/refresh")
                .header(AuthorizationHelper.AUTHORIZATION, AuthorizationHelper.BEARER + accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }
}