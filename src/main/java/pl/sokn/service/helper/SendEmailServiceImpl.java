package pl.sokn.service.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pl.sokn.dto.EmailMessage;
import pl.sokn.dto.UserDTO;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;


@Service
public class SendEmailServiceImpl implements SendEmailService {

    private static final String EMAIL = "gpf.ver.1.0@gmail.com";
    private final JavaMailSender emailSender;

    @Autowired
    public SendEmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendSimpleMessage(final String to, final String subject, final String text) {
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(EMAIL);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        emailSender.send(message);
    }

    @Override
    public void sendSimpleMessage(EmailMessage userMessage) {
        final String SUBJECT = userMessage.getName() + " wrote a message !";
        final String TEXT = userMessage.getText() + "\n\n" + userMessage.getEmail();
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(EMAIL);
        message.setTo(EMAIL);
        message.setSubject(SUBJECT);
        message.setText(TEXT);

        emailSender.send(message);
    }

    @Override
    public void sendMessageWithAttachment(final String to, final String subject, final String text, final String pathToAttachment) throws MessagingException {
        final MimeMessage message = emailSender.createMimeMessage();
        // pass 'true' receiver the constructor receiver create a multipart message
        final MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(EMAIL);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        final FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
        helper.addAttachment("Invoice", file);

        emailSender.send(message);
    }

    @Override
    public void constructRegistrationEmail(String contextPath, String token, UserDTO user) {
        final String recipientAddress = user.getEmail();
        final String SUBJECT = "Registration Confirmation";
        final String confirmationUrl
                = contextPath + "/#/user/activate/" + token;
        final String message = "\nConfirm email:\n" + confirmationUrl;

        sendSimpleMessage(recipientAddress, SUBJECT, message);
    }

    @Override
    public void constructForgotPasswordTokenEmail(String contextPath, String token, UserDTO user) {
        final String recipientAddress = user.getEmail();
        final String SUBJECT = "Reset Password";
        final String confirmationUrl
                = contextPath + "/#/user/reset/change/" + user.getId() + "/" + token;
        final String message = "Your reset password URL: \n" + confirmationUrl;

        sendSimpleMessage(recipientAddress, SUBJECT, message);
    }
}
