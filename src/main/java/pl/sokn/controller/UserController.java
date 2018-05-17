package pl.sokn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sokn.definitions.SoknDefinitions;
import pl.sokn.dto.CustomResponseMessage;
import pl.sokn.dto.EmailMessage;
import pl.sokn.dto.UserDTO;
import pl.sokn.entity.User;
import pl.sokn.exception.OperationException;
import pl.sokn.service.UserService;

import java.util.Collection;

@Api(tags = "User")
@RestController
@RequestMapping(path = SoknDefinitions.Api.USERS_PATH)
public class UserController extends AbstractGenericController<UserDTO, User, Long>{
    private UserService userService;

    @Autowired
    public UserController(@pl.sokn.annotation.qualifier.UserService UserService userService){
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

    @ApiOperation(value = "Send contact e-mail")
    @PostMapping(path="/sendContactEmail",consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity sendContactEmail(@RequestBody EmailMessage emailDTO, final HttpServletRequest request) throws OperationException {
        userService.sendEmail(emailDTO,request);
        return ResponseEntity.status(HttpStatus.OK).body(new CustomResponseMessage<>(HttpStatus.OK,""));
    }

    @ApiOperation(value = "Add reviewer authority to existing account")
    @PostMapping(path = "/addReviewerAuthority")
    public ResponseEntity addReviewerAuthority(@RequestParam("email") String email,
                                               @RequestParam("fieldObligatory") String fieldObligatory,
                                               @RequestParam(value = "fieldAdditional", required = false) String fieldAdditional, final HttpServletRequest request) throws OperationException{
        userService.addReviewerAuthority(email, fieldObligatory, fieldAdditional);
        return ResponseEntity.status(HttpStatus.OK).body(new CustomResponseMessage<>(HttpStatus.OK, ""));
    }
}
