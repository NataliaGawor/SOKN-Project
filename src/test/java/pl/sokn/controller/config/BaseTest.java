package pl.sokn.controller.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import pl.sokn.dto.UserDTO;
import pl.sokn.enums.Gender;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

public abstract class BaseTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    protected MockMvc mvc;

    protected String write(final Object object) throws JsonProcessingException {

        return mapper.writeValueAsString(object);
    }

    protected static JsonObject read(final String jsonObjectStr) {

        final JsonReader jsonReader = Json.createReader(new StringReader(jsonObjectStr));
        final JsonObject object = jsonReader.readObject();
        jsonReader.close();

        return object;
    }

    protected String obtainToken(final String email, final String password) throws Exception {

        final AccessTokenProvider tokenProvider = new AccessTokenProvider(mvc);

        return tokenProvider.obtainAccessToken(email, password);
    }


    protected UserDTO createUser() {
        final String firstName = RandomString.make(6);
        final String password = RandomString.make(8);
        return  UserDTO.builder()
                .firstName(firstName)
                .lastName(RandomString.make(6))
                .email(firstName + "@email.com")
                .enabled(true)
                .password(password)
                .matchingPassword(password)
                .degree("Mgr")
                .gender(Gender.FEMALE)
                .affiliation("Single")
                .city("Kraków")
                .country("Poland")
                .build();
    }
}
