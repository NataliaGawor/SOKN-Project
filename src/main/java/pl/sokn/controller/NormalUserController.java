package pl.sokn.controller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sokn.entity.User;
import pl.sokn.exception.OperationException;
import pl.sokn.service.UserService;
import pl.sokn.service.helper.SendEmailService;
import javax.servlet.http.HttpServletRequest;

@Api(tags="Normal user")
@RestController
@RequestMapping
public class NormalUserController{

    private UserService userService;
    private SendEmailService sendEmailService;

    public NormalUserController(@pl.sokn.annotation.qualifier.UserService UserService userService, SendEmailService sendEmailService) {
        this.userService = userService;
        this.sendEmailService = sendEmailService;
    }

    @ApiOperation(value = "Check if person is registered")
    @PostMapping(path="/checkIfRegistered",consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity checkIfRegistered(@RequestBody String email,
                                            final HttpServletRequest request) throws OperationException {

        User user=userService.retrieveByEmail(email);
        if(user!=null){
            //sendEmailService.sendSimpleMessage(email,"Informacja o posiadaniu konta","Posiadasz konto w naszym serwisie");
        }
        else{
            sendEmailService.sendSimpleMessage(email,"Informacja o posiadaniu konta","Nie posiadasz konta w naszym serwisie");
        }

        return ResponseEntity.ok(user);
    }
}
