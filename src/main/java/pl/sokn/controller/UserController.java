package pl.sokn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
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
import pl.sokn.service.implementation.UserServiceImpl;

@Api(tags = "User")
@RestController
@RequestMapping(path = SoknDefinitions.Api.USERS_PATH)
public class UserController extends AbstractGenericController<UserDTO, User, Long>{
    private UserServiceImpl userService;

    @Autowired
    public UserController(@pl.sokn.annotation.qualifier.UserService UserServiceImpl userService){
        super(userService);
        this.userService = userService;
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
    public ResponseEntity checkIfRegistered(@RequestBody String email, final HttpServletRequest request) throws OperationException {

        return ResponseEntity.ok(userService.isUserInDB(email,request));
    }
}
