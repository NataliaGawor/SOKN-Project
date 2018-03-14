package pl.sokn.service.helper;

public interface JSONCreatorService {
    String constructRegistrationResponse(final String token, final String email);

    String constructResetTokenResponse(String token, String email);
}
