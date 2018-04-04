package pl.sokn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sokn.definitions.SoknDefinitions;
import pl.sokn.dto.UserDTO;
import pl.sokn.entity.User;
import pl.sokn.exception.OperationException;
import pl.sokn.service.UserService;
import pl.sokn.service.helper.SendEmailService;

@Api(tags = "User")
@RestController
@RequestMapping(path = SoknDefinitions.Api.USERS_PATH)
public class UserController extends AbstractGenericController<UserDTO, User, Long>{
    private UserService userService;
    private SendEmailService sendEmailService;


    @Autowired
    public UserController(@pl.sokn.annotation.qualifier.UserService UserService userService, SendEmailService sendEmailService) {
        super(userService);
        this.userService = userService;
        this.sendEmailService = sendEmailService;
    }

    @Override
    protected UserDTO convertToDTO(final User user) {
        return User.convertTo(user);
    }

    @Override
    protected User convertToEntity(final UserDTO user) {
        return User.convertFrom(user);
    }

    @ApiOperation(value = "Check if person is registered")
    @PostMapping(path="/checkIfRegistered",consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity checkIfRegistered(@RequestBody String email) throws OperationException {

        User user=userService.retrieveByEmail(email);
        if(user!=null){
            sendEmailService.sendSimpleMessage(email,"Informacja o posiadaniu konta","Posiadasz konto w naszym serwisie. "+
                    "Aby sie zalogowac kliknij-->" +"<a href="+"http:"+"//85.255.11.29:8080/logIn/logIn.html"+">Zaloguj se</a>");
        }
        else{
            sendEmailService.sendSimpleMessage(email,"Informacja o posiadaniu konta","Nie posiadasz konta w naszym serwisie. " +
                    "Aby sie zarejestrowac kliknij--><a href="+"http:"+"//85.255.11.29:8080/registration/registration.html"+">Rejestracja</a>");

        }
        return ResponseEntity.ok(user);
    }
}
