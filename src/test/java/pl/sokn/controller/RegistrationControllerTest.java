package pl.sokn.controller;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import pl.sokn.controller.config.BaseTest;
import pl.sokn.definitions.SoknDefinitions.Api;
import pl.sokn.definitions.SoknDefinitions.ApiMessages;
import pl.sokn.definitions.SoknDefinitions.AuthorizationHelper;
import pl.sokn.dto.PasswordUpdate;
import pl.sokn.dto.UserDTO;

import javax.json.JsonObject;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationControllerTest extends BaseTest {

    @Test
    public void registerAndEnableUser() throws Exception {
        final UserDTO user = createUser();

        final String object = write(user);

        String result = mvc.perform(post(Api.REGISTER)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(object))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        JsonObject jsonObject = read(result);

        final String token = jsonObject.getString("token");

        result = mvc.perform(get(Api.REGISTRATION_CONFIRM + "/" + token)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        jsonObject = read(result);
        assertThat(jsonObject.getString("message"), Matchers.is(ApiMessages.EMAIL_CONFIRMED));
    }

    @Test
    public void shouldFailOnRegistration() throws Exception {
        final UserDTO user = UserDTO.builder().build();

        mvc.perform(post(Api.REGISTER)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(write(user)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void shouldResetPassword() throws Exception {
        final String token = obtainToken("author@email.com", "pass");
        final PasswordUpdate password = new PasswordUpdate();
        password.setOldPassword("pass");
        password.setPassword("new");
        password.setMatchingPassword("new");

        mvc.perform(post(Api.USERS_PATH + "/updatePassword")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(write(password))
                .header(AuthorizationHelper.AUTHORIZATION, AuthorizationHelper.BEARER + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

}