package pl.sokn.service.helper;

import pl.sokn.dto.EmailMessage;
import pl.sokn.entity.User;

import javax.mail.MessagingException;

/**
 * Service responsible for sending emails
 */
public interface SendEmailService {
    void sendSimpleMessage(final String to, final String subject, final String text);

    void sendSimpleMessage(EmailMessage userMessage);

    void sendMessageWithAttachment(final String to, final String subject, final String text, final String pathToAttachment) throws MessagingException;

    void constructRegistrationEmail(final String contextPath, final String token, final User user);

    void constructForgotPasswordTokenEmail(final String contextPath, final String token, final User user);
}
