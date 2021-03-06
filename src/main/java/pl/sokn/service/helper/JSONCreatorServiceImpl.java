package pl.sokn.service.helper;

import org.springframework.stereotype.Service;
import pl.sokn.definitions.SoknDefinitions.ApiMessages;

import javax.json.Json;
import java.text.MessageFormat;

@Service
public class JSONCreatorServiceImpl implements JSONCreatorService {

    @Override
    public String constructRegistrationResponse(final String token, final String email) {

        return createJsonForRegistrationProcess(token, email, ApiMessages.REGISTRATION_TOKEN_SEND);
    }

    @Override
    public String constructResetTokenResponse(final String token, final String email) {
        return createJsonForRegistrationProcess(token, email, ApiMessages.RESET_TOKEN_SEND);
    }

    private String createJsonForRegistrationProcess(final String token, final String email, final String message) {
        return Json.createObjectBuilder()
                .add("token", token)
                .add("email", email)
                .add("message", MessageFormat.format(message, email))
                .build().toString();
    }
}
