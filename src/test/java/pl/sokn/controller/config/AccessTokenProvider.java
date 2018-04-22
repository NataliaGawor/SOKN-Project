package pl.sokn.controller.config;

import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.json.Json;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.sokn.definitions.SoknDefinitions.*;

public class AccessTokenProvider {
    private MockMvc mockMvc;

    AccessTokenProvider(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    String obtainAccessToken(final String email, final String password) throws Exception {
        final String body = Json.createObjectBuilder()
                .add("email", email)
                .add("password", password)
                .build()
                .toString();

        final ResultActions result =
                mockMvc.perform(post(Api.LOGIN)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

        final String response = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(response).get("token").toString();
    }
}
