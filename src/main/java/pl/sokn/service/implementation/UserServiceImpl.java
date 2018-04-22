package pl.sokn.service.implementation;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.sokn.dto.EmailMessage;
import pl.sokn.entity.User;
import pl.sokn.exception.OperationException;
import pl.sokn.repository.PasswordResetTokenRepository;
import pl.sokn.repository.TokenRepository;
import pl.sokn.repository.UserRepository;
import pl.sokn.service.AuthorityService;
import pl.sokn.service.UserService;
import pl.sokn.service.helper.SendEmailService;


@Service
@pl.sokn.annotation.qualifier.UserService
public class UserServiceImpl extends AbstractGenericService<User, Long> implements UserService {

    final UserRepository userRepository;
    final AuthorityService authorityService;
    final PasswordEncoder passwordEncoder;
    final TokenRepository tokenRepository;
    final PasswordResetTokenRepository passwordResetTokenRepository;
    private SendEmailService sendEmailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           AuthorityService authorityService,
                           PasswordEncoder passwordEncoder,
                           TokenRepository tokenRepository,
                           PasswordResetTokenRepository passwordResetTokenRepository,
                           SendEmailService sendEmailService) {
        super(userRepository);
        this.userRepository = userRepository;
        this.authorityService = authorityService;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.sendEmailService=sendEmailService;
    }

    @Override
    public User retrieveByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    boolean isUserInDB(final User user) {
        return retrieveByEmail(user.getEmail()) != null;
    }

    @Override
    public User isUserInDB(final String email, final HttpServletRequest request)throws OperationException{
        User user=retrieveByEmail(email);
        String url=getAppUrl(request);
        if(user!=null){
            sendEmailService.sendSimpleMessage(email,"Informacja o posiadaniu konta","Posiadasz konto w naszym serwisie. "+
                    "Aby sie zalogowac kliknij-->" +"<a href="+url+"/CustomUser/logIn/logIn.html"+">Zaloguj se</a>");
        }
        else{
            sendEmailService.sendSimpleMessage(email,"Informacja o posiadaniu konta","Nie posiadasz konta w naszym serwisie. " +
                    "Aby sie zarejestrowac kliknij--><a href="+url+"/CustomUser/registration/registration.html"+">Rejestracja</a>");
        }
        return user;
    }

    //Send Email to sokn.noreply
    @Override
    public void sendEmail(final EmailMessage emailDTO, final HttpServletRequest request)throws OperationException{
        String email=emailDTO.getEmail();
        String subject = emailDTO.getName();
        String message = emailDTO.getText();

        sendEmailService.sendSimpleMessage("sokn.noreply@gmail.com",subject,"E-mail from: "+ email+"<br/>"+message);
    }
}
