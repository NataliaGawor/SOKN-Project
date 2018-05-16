package pl.sokn.service.helper;

import pl.sokn.dto.EmailMessage;
import pl.sokn.entity.User;
import pl.sokn.exception.OperationException;

import javax.mail.MessagingException;

/**
 * Service responsible for sending emails
 */
public interface SendEmailService {
    void sendSimpleMessage(final String to, final String subject, final String text) throws OperationException;

    void sendSimpleMessage(EmailMessage userMessage);

    void sendMessageWithAttachment(final String to, final String subject, final String text, final String pathToAttachment) throws MessagingException;

    void constructRegistrationEmail(final String contextPath, final String token, final User user) throws OperationException;

    void constructForgotPasswordTokenEmail(final String contextPath, final String token, final User user) throws OperationException;

    void constructReviewerRegistrationEmail(final String to, String password) throws OperationException;
}
